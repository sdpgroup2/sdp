# import numpy as np
import cv2
import cv


mins = {
    'H': 0,
    'S': 0,
    'V': 0
}


def nothing(channel):
    def update(x):
        mins[channel] = x
    return update


def get_point(event, x, y, flags, param):
    pass


cap = cv2.VideoCapture(0)

cv2.namedWindow('image')
cv2.setMouseCallback('image', get_point)

cv2.createTrackbar('H', 'image', 0, 180, nothing('H'))
cv2.createTrackbar('S', 'image', 0, 255, nothing('S'))
cv2.createTrackbar('V', 'image', 0, 255, nothing('V'))

ret, frame = cap.read()
out = cv.CreateImage((frame.shape[0], frame.shape[1]), cv.IPL_DEPTH_8U, 3)

while(True):
    ret, frame = cap.read()
    cv.InRangeS(
        out,
        cv.Scalar(mins['H'], mins['S'], mins['V']),
        cv.Scalar(255, 255, 255),
        out
    )
    cv2.imshow('image', frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
