import cv2
import glob
import os
import numpy as np

prototxt_path = "deploy.prototxt.txt"
model_path = "res10_300x300_ssd_iter_140000_fp16.caffemodel"
model = cv2.dnn.readNetFromCaffe(prototxt_path, model_path)


def crop_image(image):
    h, w = image.shape[:2]

    blob = cv2.dnn.blobFromImage(image, 1.0, (300, 300), (104.0, 177.0, 123.0))

    model.setInput(blob)
    output = np.squeeze(model.forward())

    result_image = image
    good_images_counter = 0
    for i in range(0, output.shape[0]):
        confidence = output[i, 2]

        if confidence > 0.5:
            good_images_counter += 1
            box = output[i, 3:7] * np.array([w, h, w, h])
            start_x, start_y, end_x, end_y = box.astype(np.int_)

            crop_width = h / 1.5
            face_horizontal_center = (start_x + end_x) / 2
            crop_x_start = int(face_horizontal_center - crop_width / 2)
            crop_x_end = int(face_horizontal_center + crop_width / 2)
            margin = 0
            if crop_x_start < 0:
                margin = abs(crop_x_start)
            elif crop_x_end > w:
                margin = (crop_x_end - w) * -1
            if crop_x_start + margin < 0 or crop_x_end + margin > w:
                face_vertical_center = (start_y + end_y) / 2
                crop_height = w * 1.5
                crop_y_start = int(face_vertical_center - crop_height / 2)
                crop_y_end = int(face_vertical_center + crop_height / 2)
                margin = 0
                if crop_y_start < 0:
                    margin = abs(crop_y_start)
                elif crop_y_end > h:
                    margin = (crop_y_end - h) * -1
                crop_img = image[crop_y_start + margin:crop_y_end + margin, 0:int(w)].copy()
            else:
                crop_img = image[0:int(h), crop_x_start + margin:crop_x_end + margin].copy()

            result_image = crop_img

    if good_images_counter == 1:
        return result_image
    else:
        return None


all_images = glob.glob("./images/*")
if not os.path.exists("./cropped"):
    os.makedirs("./cropped")
if not os.path.exists("./not_cropped"):
    os.makedirs("./not_cropped")

for img in all_images:
    file_name = os.path.basename(img)
    image_to_crop = cv2.imread(img)
    try:
        cropped_img = crop_image(image_to_crop)
    except BaseException as e:
        print("Exception on file: " + file_name)
        print(e)
        continue

    if cropped_img is None:
        print("File: " + file_name + " cannot be simply cropped.")
        cv2.imwrite("./not_cropped/" + file_name, image_to_crop)
    else:
        cv2.imwrite(os.path.join("./cropped", file_name), cropped_img)
