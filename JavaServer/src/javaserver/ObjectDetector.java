package javaserver;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * 
 */

/**
 * @author Dua
 *
 */
public class ObjectDetector extends Detector {
	public ObjectDetector(String _modelConfigurationPath, String _modelBinaryPath) {
		super(0.007843f, 127.5f, 0.2f, _modelConfigurationPath, _modelBinaryPath);
		String className[] = { "background", "aeroplane", "bicycle", "bird", "boat", "bottle", "bus", "car", "cat",
				"chair", "cow", "diningtable", "dog", "horse", "motorbike", "person", "pottedplant", "sheep", "sofa",
				"train", "tvmonitor" };
		this._className = className;
	}

	@Override
	public void predict(Mat image, boolean isDrawBox) {
		// TODO Auto-generated method stub
		
		int[] nrowss = {0};
		float[] floatBuffer = forWardNet(image, nrowss);
		int nrows = nrowss[0];
		
		for (int i = 0; i < nrows; i++) {
			try {

				int objectClass = (int) floatBuffer[i * 7 + 1];
				double confidence = (double) floatBuffer[i * 7 + 2];

				if (confidence > _confidenceThreshold && objectClass != 0 && isDrawBox) {
					int left = (int) (floatBuffer[i * 7 + 3] * image.cols());
					int top = (int) (floatBuffer[i * 7 + 4] * image.rows());
					int right = (int) (floatBuffer[i * 7 + 5] * image.cols());
					int bottom = (int) (floatBuffer[i * 7 + 6] * image.rows());

					Imgproc.rectangle(image, new Point(left, top), new Point(right, bottom), new Scalar(100, 200, 0),
							3);
					
					String label = _className[objectClass] + ": " + (float) ((int) (confidence * 1000)) / 1000;
					int baseLine = 5;
					Size labelSize = new Size(100, 10);
					top = Math.max(top, (int) labelSize.height);
					Imgproc.rectangle(image, new Point(left, top - labelSize.height),
							new Point(left + labelSize.width, top + baseLine), new Scalar(255, 255, 255), Core.FILLED);
					Imgproc.putText(image, label, new Point(left, top), Core.FONT_HERSHEY_TRIPLEX, 0.5,
							new Scalar(0, 0, 255));
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}

	}

	public void setClassName(String[] _className) {
		this._className = _className;
	}

//	private String _className[];
}
