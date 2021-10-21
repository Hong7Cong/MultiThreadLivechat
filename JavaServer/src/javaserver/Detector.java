package javaserver;

import org.opencv.dnn.*;
import org.opencv.core.*;

/**
 * 
 */

/**
 * @author Dua
 
 *
 */
public abstract class Detector {

	public Detector(float _inScaleFactor, float _meanVal, float _confidenceThreshold, String _modelConfigurationPath,
			String _modelBinaryPath) {
		this._inScaleFactor = _inScaleFactor;
		this._meanVal = new Scalar(_meanVal, _meanVal, _meanVal);
		this._confidenceThreshold = _confidenceThreshold;
		this._modelConfigurationPath = _modelConfigurationPath;
		this._modelBinaryPath = _modelBinaryPath;
		System.out.println("Initializing model .....");
		this._nnet = Dnn.readNetFromCaffe(this._modelConfigurationPath, this._modelBinaryPath);
		if (!_nnet.empty()) {
			System.out.println("Loading model is completed!");
		} else {
			System.out.println("Can't load model ....");
		}
	}

	public Detector(float _inScaleFactor, Scalar _meanVal, float _confidenceThreshold, String _modelConfigurationPath,
			String _modelBinaryPath) {
		this._inScaleFactor = _inScaleFactor;
		this._meanVal = _meanVal;
		this._confidenceThreshold = _confidenceThreshold;
		this._modelConfigurationPath = _modelConfigurationPath;
		this._modelBinaryPath = _modelBinaryPath;
		System.out.println("Initializing model .....");
		this._nnet = Dnn.readNetFromCaffe(this._modelConfigurationPath, this._modelBinaryPath);
		if (!_nnet.empty()) {
			System.out.println("Loading model is completed!");
		} else {
			System.out.println("Can't load model ....");
		}
	}
	
	public float[] forWardNet(Mat image, int nrows[]) {
		// nrows = new int[1];
		Mat inputBlob = Dnn.blobFromImage(image, _inScaleFactor, new Size(_inWidth, _inHeigh), _meanVal, false, false);
		_nnet.setInput(inputBlob,"data");
		Mat detectedResults = _nnet.forward("detection_out");

		// javadoc: Mat::reshape(cn, rows)
		detectedResults = detectedResults.reshape(1, (int) detectedResults.total() / 7);
		if (detectedResults.empty()) {
			System.out.println("Result is empty!");
		}

		nrows[0] = detectedResults.rows();
		int numChannels = detectedResults.channels();// is 3 for 8UC3 (e.g. RGB)
		int resSize = detectedResults.rows() * detectedResults.cols();
		float[] floatBuffer = new float[resSize * numChannels];

		detectedResults.get(0, 0, floatBuffer);

		// the number of rows correspond with number of boxes
		return floatBuffer;

	}

	public void set_confidenceThreshold(float _confidenceThreshold) {
		this._confidenceThreshold = _confidenceThreshold;
	}

	public void set_inScaleFactor(float _inScaleFactor) {
		this._inScaleFactor = _inScaleFactor;
	}
	
	public void set_meanVal(float _meanVal) {
		this._meanVal = new Scalar(_meanVal, _meanVal, _meanVal);
	}
	
	public void set_meanVal(float _meanVal,float _meanVal1,float _meanVal2) {
		this._meanVal = new Scalar(_meanVal, _meanVal1, _meanVal2);
	}

	public abstract void predict(Mat image, boolean isDrawBox);

	private static final int _inWidth = 300;
	private static final int _inHeigh = 300;
//	private static final float WH_RATIO = (float)_inWidth / _inHeigh;
	private String _modelConfigurationPath;
	private String _modelBinaryPath;
	private Net _nnet;
	private float _inScaleFactor;
	private Scalar _meanVal;

	protected float _confidenceThreshold;
	public String _className[];
}
