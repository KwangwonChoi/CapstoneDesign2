import cv2

def detection_face_harr(image) :
    faceCascade = cv2.CascadeClassifier('models/haarcascade_frontface.xml')
    gray = image;
    faces = faceCascade.detectMultiScale(
        gray,
        scaleFactor=1.3,  # 이미지 피라미드에서 사용하는 scaleFactor
        minNeighbors=1,  # 이미지 피라미드에 의한 여러 스케일의 크기에서 minNeighbors 횟수 이상 검출된 object는 valid하게 검출
        minSize=(2, 2)
    )

    for (x, y, w, h) in faces:
        cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)

    return faces, image
