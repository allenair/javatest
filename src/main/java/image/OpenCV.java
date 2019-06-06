package image;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpenCV {
    static{
    	System.out.println(System.getProperty("java.library.path"));
    }
    public static void main(String[] args) {
        System.out.println("Welcome to OpenCV " + Core.VERSION);
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // bug
        System.loadLibrary("opencv_java410");
        Mat m  = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("m = " + m.dump());
    }
}
