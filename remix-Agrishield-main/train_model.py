#!/usr/bin/env python3
"""
AgriShield Cotton Disease Model Training Pipeline v2.0
TF 2.11-2.21 compatible, Keras 2 & Keras 3, CPU-safe.
Run: python download_dataset.py  then  python train_model.py
"""
import os, sys, json, shutil, argparse, time
import numpy as np
from pathlib import Path
os.environ["TF_CPP_MIN_LOG_LEVEL"] = "2"
os.environ["TF_ENABLE_ONEDNN_OPTS"] = "0"
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import tensorflow as tf
from sklearn.metrics import classification_report, confusion_matrix
import seaborn as sns
print(f"TF {tf.__version__} | GPU: {bool(tf.config.list_physical_devices('GPU'))}")

IMG_SIZE=224; BATCH_SIZE=32; EPOCHS_HEAD=10; EPOCHS_FINE=20
LR_HEAD=1e-3; LR_FINE=5e-6; VAL_SPLIT=0.15
DATA_ROOT=Path("data/Cotton Disease"); MODEL_DIR=Path("models"); MODEL_DIR.mkdir(exist_ok=True)

CLASS_METADATA={
 "diseased cotton leaf":{"display_name":"Diseased Cotton Leaf","marathi":"रोगग्रस्त कापूस पान","hindi":"रोगग्रस्त कपास पत्ती","severity":"HIGH","immediate_action":"Isolate affected section. Apply Trichoderma viride 5g/L. Scout 20 plants/acre.","likely_causes":["Alternaria leaf spot","Grey mildew","Bacterial blight","Nutrient deficiency"],"chemical":"Mancozeb 75 WP 2.0g/L PHI 5 days","biological":"Trichoderma viride 5g/L + Pseudomonas fluorescens 2g/L"},
 "diseased cotton plant":{"display_name":"Diseased Cotton Plant","marathi":"रोगग्रस्त कापूस रोप","hindi":"रोगग्रस्त कपास पौधा","severity":"CRITICAL","immediate_action":"Entire plant infected. Check stem = Fusarium wilt; rosette flowers = Pink Bollworm; leaf curl = Whitefly CLCuV. Uproot severely wilted plants.","likely_causes":["Fusarium wilt","Pink Bollworm","Whitefly CLCuV","Helicoverpa bollworm"],"chemical":"Chlorantraniliprole 18.5% SC 0.3ml/L PHI 21 days (pest) OR Cymoxanil+Mancozeb 2g/L (fungal)","biological":"Trichogramma bactrae 60000/acre + Gossyplure pheromone traps 8/acre"},
 "fresh cotton leaf":{"display_name":"Healthy Cotton Leaf","marathi":"निरोगी कापूस पान","hindi":"स्वस्थ कपास पत्ती","severity":"LOW","immediate_action":"Plant healthy. Scout every 7 days. Check pheromone trap ETL (8 moths/night = action threshold).","likely_causes":[],"chemical":"No intervention needed","biological":"Preventive NSKE 5% spray as per schedule"},
 "fresh cotton plant":{"display_name":"Healthy Cotton Plant","marathi":"निरोगी कापूस रोप","hindi":"स्वस्थ कपास पौधा","severity":"LOW","immediate_action":"Plant healthy. Continue NPK fertilizer schedule. Scout 20 plants/acre weekly.","likely_causes":[],"chemical":"No intervention","biological":"Release Trichogramma 50000/acre as routine biocontrol"},
}

def prepare_data():
    train_dir=DATA_ROOT/"train"; val_dir=DATA_ROOT/"val"; test_dir=DATA_ROOT/"test"
    if not train_dir.exists():
        print(f"ERROR: {train_dir} not found. Run: python download_dataset.py"); sys.exit(1)
    if not val_dir.exists() or not any(val_dir.rglob("*.jpg")):
        print("Creating val/ split (15%)...")
        val_dir.mkdir(exist_ok=True)
        for cd in sorted(train_dir.iterdir()):
            if not cd.is_dir(): continue
            imgs=sorted(list(cd.glob("*.jpg"))+list(cd.glob("*.jpeg"))+list(cd.glob("*.png")))
            if not imgs: continue
            n_val=max(1,int(len(imgs)*VAL_SPLIT))
            vd=val_dir/cd.name; vd.mkdir(exist_ok=True)
            for img in imgs[-n_val:]: shutil.copy(str(img),str(vd/img.name))
    return train_dir, val_dir, test_dir if test_dir.exists() else val_dir

def build_datasets(train_dir,val_dir,test_dir):
    kwargs=dict(image_size=(IMG_SIZE,IMG_SIZE),batch_size=BATCH_SIZE,label_mode="categorical",seed=42)
    train_ds=tf.keras.utils.image_dataset_from_directory(str(train_dir),shuffle=True,**kwargs)
    val_ds=tf.keras.utils.image_dataset_from_directory(str(val_dir),shuffle=False,**kwargs)
    test_ds=tf.keras.utils.image_dataset_from_directory(str(test_dir),shuffle=False,**kwargs)
    class_names=train_ds.class_names
    print(f"Classes: {class_names}"); print(f"Train batches: {len(train_ds)} | Val: {len(val_ds)} | Test: {len(test_ds)}")
    AUT=tf.data.AUTOTUNE
    aug=tf.keras.Sequential([
        tf.keras.layers.RandomFlip("horizontal_and_vertical"),
        tf.keras.layers.RandomRotation(0.3),
        tf.keras.layers.RandomZoom(0.25),
        tf.keras.layers.RandomBrightness(0.3),
        tf.keras.layers.RandomContrast(0.3),
        tf.keras.layers.RandomTranslation(0.15,0.15),
    ],name="aug")
    norm=tf.keras.layers.Rescaling(1./255)
    def pt(x,y): return norm(aug(x,training=True)),y
    def pe(x,y): return norm(x),y
    train_ds=(train_ds.map(pt,num_parallel_calls=AUT).prefetch(AUT))
    val_ds=(val_ds.map(pe,num_parallel_calls=AUT).prefetch(AUT))
    test_ds=(test_ds.map(pe,num_parallel_calls=AUT).prefetch(AUT))
    return train_ds,val_ds,test_ds,class_names

def build_model(n):
    base=tf.keras.applications.MobileNetV2(input_shape=(IMG_SIZE,IMG_SIZE,3),include_top=False,weights="imagenet")
    base.trainable=False
    inp=tf.keras.Input(shape=(IMG_SIZE,IMG_SIZE,3))
    x=base(inp,training=False)
    x=tf.keras.layers.GlobalAveragePooling2D()(x)
    x=tf.keras.layers.BatchNormalization()(x)
    x=tf.keras.layers.Dense(256,activation="relu",kernel_regularizer=tf.keras.regularizers.l2(1e-4))(x)
    x=tf.keras.layers.Dropout(0.45)(x)
    x=tf.keras.layers.Dense(128,activation="relu",kernel_regularizer=tf.keras.regularizers.l2(1e-4))(x)
    x=tf.keras.layers.Dropout(0.3)(x)
    out=tf.keras.layers.Dense(n,activation="softmax")(x)
    m=tf.keras.Model(inp,out)
    m.compile(optimizer=tf.keras.optimizers.Adam(LR_HEAD),loss="categorical_crossentropy",metrics=["accuracy"])
    return m,base

def unfreeze(m,base,n=30):
    base.trainable=True
    for l in base.layers[:-n]: l.trainable=False
    m.compile(optimizer=tf.keras.optimizers.Adam(LR_FINE),loss="categorical_crossentropy",metrics=["accuracy"])
    return m

def train(train_ds,val_ds,nc):
    m,base=build_model(nc)
    cb1=[tf.keras.callbacks.EarlyStopping(monitor="val_accuracy",patience=4,restore_best_weights=True),
         tf.keras.callbacks.ReduceLROnPlateau(monitor="val_loss",factor=0.5,patience=2,min_lr=1e-7),
         tf.keras.callbacks.ModelCheckpoint(str(MODEL_DIR/"best_head.keras"),monitor="val_accuracy",save_best_only=True)]
    print("\n=== Phase 1: head training (base frozen) ===")
    h1=m.fit(train_ds,epochs=EPOCHS_HEAD,validation_data=val_ds,callbacks=cb1)
    m=unfreeze(m,base,30)
    cb2=[tf.keras.callbacks.EarlyStopping(monitor="val_accuracy",patience=6,restore_best_weights=True),
         tf.keras.callbacks.ReduceLROnPlateau(monitor="val_loss",factor=0.3,patience=3,min_lr=1e-8),
         tf.keras.callbacks.ModelCheckpoint(str(MODEL_DIR/"best_finetuned.keras"),monitor="val_accuracy",save_best_only=True)]
    print("\n=== Phase 2: fine-tuning top 30 layers ===")
    h2=m.fit(train_ds,epochs=EPOCHS_HEAD+EPOCHS_FINE,initial_epoch=len(h1.epoch),validation_data=val_ds,callbacks=cb2)
    hist={k:h1.history[k]+h2.history.get(k,[]) for k in h1.history}
    return m,hist

def evaluate(m,test_ds,cn):
    yp,yt=[],[]
    for x,y in test_ds:
        yp.extend(m.predict(x,verbose=0)); yt.extend(np.argmax(y.numpy(),axis=1))
    yp=np.argmax(yp,axis=1); yt=np.array(yt)
    print(classification_report(yt,yp,target_names=cn))
    cm=confusion_matrix(yt,yp)
    fig,ax=plt.subplots(figsize=(8,6))
    sns.heatmap(cm,annot=True,fmt="d",cmap="Greens",xticklabels=cn,yticklabels=cn,ax=ax)
    ax.set_title("AgriShield Cotton — Confusion Matrix"); plt.tight_layout()
    fig.savefig(str(MODEL_DIR/"confusion_matrix.png"),dpi=150); plt.close(fig)
    r=classification_report(yt,yp,target_names=cn,output_dict=True)
    with open(MODEL_DIR/"eval_report.json","w") as f: json.dump(r,f,indent=2)
    return r

def plot_history(h):
    fig,(a1,a2)=plt.subplots(1,2,figsize=(14,5))
    a1.plot(h["accuracy"],label="Train"); a1.plot(h["val_accuracy"],label="Val"); a1.set_title("Accuracy"); a1.legend()
    a2.plot(h["loss"],label="Train"); a2.plot(h["val_loss"],label="Val"); a2.set_title("Loss"); a2.legend()
    plt.tight_layout(); fig.savefig(str(MODEL_DIR/"training_history.png"),dpi=150); plt.close(fig)

def export_tflite(m,cn):
    sp=str(MODEL_DIR/"saved_model")
    # Keras 3 API: use model.export() for TFLite-compatible SavedModel
    try:
        m.export(sp)
        print(f"SavedModel exported -> {sp}")
        c1=tf.lite.TFLiteConverter.from_saved_model(sp); t1=c1.convert()
    except Exception as e1:
        print(f"export() failed ({e1}), trying from_keras_model...")
        try:
            c1=tf.lite.TFLiteConverter.from_keras_model(m); t1=c1.convert()
        except Exception as e2:
            print(f"from_keras_model failed ({e2}), saving .keras then loading...")
            kp=str(MODEL_DIR/"best_model.keras"); m.save(kp)
            m2=tf.keras.models.load_model(kp)
            c1=tf.lite.TFLiteConverter.from_keras_model(m2); t1=c1.convert()
    p1=MODEL_DIR/"agrishield_cotton.tflite"; p1.write_bytes(t1)
    print(f"Float32 TFLite: {p1} ({len(t1)//1024}KB)")
    try:
        c2=tf.lite.TFLiteConverter.from_keras_model(m)
        c2.optimizations=[tf.lite.Optimize.DEFAULT]; c2.target_spec.supported_types=[tf.float16]
        t2=c2.convert(); p2=MODEL_DIR/"agrishield_cotton_fp16.tflite"; p2.write_bytes(t2)
        print(f"FP16 TFLite: {p2} ({len(t2)//1024}KB)")
    except Exception as e: print(f"FP16 export skipped: {e}")
    (MODEL_DIR/"labels.txt").write_text("\n".join(cn))
    meta={"model_version":"2.0","input_size":IMG_SIZE,"classes":{}}
    for i,name in enumerate(cn):
        meta["classes"][str(i)]={"label":name,**CLASS_METADATA.get(name,{"display_name":name,"severity":"MODERATE","immediate_action":"Consult KVK.","likely_causes":[],"chemical":"","biological":"","marathi":name,"hindi":name})}
    with open(MODEL_DIR/"model_metadata.json","w",encoding="utf-8") as f: json.dump(meta,f,indent=2,ensure_ascii=False)
    return p1

def verify_tflite(p,test_ds,cn):
    print("\n=== TFLite Verification ===")
    interp=tf.lite.Interpreter(model_path=str(p)); interp.allocate_tensors()
    inp=interp.get_input_details()[0]; out=interp.get_output_details()[0]
    print(f"Input: {inp['shape']} Output: {out['shape']}")
    correct,total=0,0
    for bx,by in test_ds.take(1):
        for i in range(min(20,len(bx))):
            img=np.expand_dims(bx[i].numpy().astype(np.float32),0)
            interp.set_tensor(inp["index"],img); interp.invoke()
            probs=interp.get_tensor(out["index"])[0]
            pred=int(np.argmax(probs)); true=int(np.argmax(by[i].numpy())); conf=probs[pred]*100
            mk="OK" if pred==true else "XX"
            print(f"  [{mk}] True:{cn[true][:20]:20s} Pred:{cn[pred][:20]:20s} Conf:{conf:.1f}%")
            if pred==true: correct+=1; total+=1
    print(f"  Batch: {correct}/{total} = {correct/max(total,1)*100:.0f}%")

def main():
    t0=time.time()
    print("\n===== AgriShield Cotton Training v2.0 =====")
    td,vd,ted=prepare_data()
    tds,vds,teds,cn=build_datasets(td,vd,ted)
    m,h=train(tds,vds,len(cn))
    plot_history(h)
    r=evaluate(m,teds,cn)
    acc=r.get("accuracy",0)
    tp=export_tflite(m,cn)
    verify_tflite(tp,teds,cn)
    elapsed=(time.time()-t0)/60
    print(f"""
===== DONE ({elapsed:.1f} min) =====
Accuracy : {acc*100:.2f}%
TFLite   : models/agrishield_cotton.tflite
Labels   : models/labels.txt
Metadata : models/model_metadata.json

NEXT: copy to Android assets/ then run gradlew assembleDebug
cp models/agrishield_cotton.tflite app/src/main/assets/
cp models/labels.txt               app/src/main/assets/
cp models/model_metadata.json      app/src/main/assets/
""")

if __name__=="__main__":
    main()
