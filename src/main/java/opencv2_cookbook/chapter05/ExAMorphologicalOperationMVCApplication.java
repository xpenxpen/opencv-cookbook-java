package opencv2_cookbook.chapter05;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Size;

/**
 * 'OpenCV 3.0 Computer Vision with Java' Chapter 3.
 * Morphological Operators
 */
public class ExAMorphologicalOperationMVCApplication {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ExAMorphologicalOperationFrame frame = new ExAMorphologicalOperationFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

class ExAMorphologicalOperationFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 800;
    private static final String erodeString = "Erode";
    private static final String dilateString = "Dilate";
    private static final String openString = "Open";
    private static final String closeString = "Close";
    private static final String rectangleString = "Rectangle";
    private static final String ellipseString = "Ellipse";
    private static final String crossString = "Cross";
    private String currentOperation = erodeString;
    private JLabel imageView;
    private Mat image;
    private int kernelSize = 0;
    private ExAMorphologicalOperationController controller = new ExAMorphologicalOperationController();;
    private int currentShape = opencv_imgproc.CV_SHAPE_RECT;
    
    public ExAMorphologicalOperationFrame() {
        image = OpenCVUtilsJava.loadAndShowOrExit(new File("data/baboon.jpg"), opencv_imgcodecs.IMREAD_COLOR);
        setTitle("Morphology Example");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        
        setupOperationRadioButtons();
        setupSizeSlider();
        setupShapeRadioButtons();
        setupImage();
        
        // Sync display for the first time
        updateView(image);
    }

    private void setupOperationRadioButtons() {
        JRadioButton erodeButton = new JRadioButton(erodeString);
        erodeButton.setMnemonic(KeyEvent.VK_E);
        erodeButton.setActionCommand(erodeString);
        erodeButton.setSelected(true);

        JRadioButton dilateButton = new JRadioButton(dilateString);
        dilateButton.setMnemonic(KeyEvent.VK_D);
        dilateButton.setActionCommand(dilateString);

        JRadioButton openButton = new JRadioButton(openString);
        openButton.setMnemonic(KeyEvent.VK_O);
        openButton.setActionCommand(openString);

        JRadioButton closeButton = new JRadioButton(closeString);
        closeButton.setMnemonic(KeyEvent.VK_C);
        closeButton.setActionCommand(closeString);

        ButtonGroup group = new ButtonGroup();
        group.add(erodeButton);
        group.add(dilateButton);
        group.add(openButton);
        group.add(closeButton);

        ActionListener operationChangeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                currentOperation = event.getActionCommand();
                processOperation();
            }
        };

        erodeButton.addActionListener(operationChangeListener);
        dilateButton.addActionListener(operationChangeListener);
        openButton.addActionListener(operationChangeListener);
        closeButton.addActionListener(operationChangeListener);

        GridLayout gridRowLayout = new GridLayout(1, 0);
        JPanel radioOperationPanel = new JPanel(gridRowLayout);

        JLabel operationLabel = new JLabel("Operation:");

        radioOperationPanel.add(erodeButton);
        radioOperationPanel.add(dilateButton);
        radioOperationPanel.add(openButton);
        radioOperationPanel.add(closeButton);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        add(operationLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        add(radioOperationPanel, c);
    }

    private void setupSizeSlider() {
        JLabel sliderLabel = new JLabel("Kernel size:", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int minimum = 0;
        int maximum = 20;
        int initial = 0;

        JSlider levelSlider = new JSlider(JSlider.HORIZONTAL, minimum, maximum, initial);
        levelSlider.setMajorTickSpacing(2);
        levelSlider.setMinorTickSpacing(1);
        levelSlider.setPaintTicks(true);
        levelSlider.setPaintLabels(true);
        
        levelSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                kernelSize = (int)source.getValue();
                processOperation();         
            }
        });


        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 1;
        add(sliderLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        add(levelSlider, c);
    }

    private void setupShapeRadioButtons() {
        JRadioButton rectangleButton = new JRadioButton(rectangleString);
        rectangleButton.setMnemonic(KeyEvent.VK_R);
        rectangleButton.setActionCommand(rectangleString);
        rectangleButton.setSelected(true);

        JRadioButton ellipseButton = new JRadioButton(ellipseString);
        ellipseButton.setMnemonic(KeyEvent.VK_L);
        ellipseButton.setActionCommand(ellipseString);

        JRadioButton crossButton = new JRadioButton(crossString);
        crossButton.setMnemonic(KeyEvent.VK_S);
        crossButton.setActionCommand(crossString);

        ButtonGroup group = new ButtonGroup();
        group.add(rectangleButton);
        group.add(ellipseButton);
        group.add(crossButton);

        ActionListener shapeChangeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String currentShapeString = event.getActionCommand();
                if(rectangleString.equals(currentShapeString)){
                    currentShape = opencv_imgproc.CV_SHAPE_RECT;
                }
                else if(ellipseString.equals(currentShapeString)){
                    currentShape = opencv_imgproc.CV_SHAPE_ELLIPSE;
                }
                else if(crossString.equals(currentShapeString)){
                    currentShape = opencv_imgproc.CV_SHAPE_CROSS;
                }
                processOperation(); 
            }
        };

        rectangleButton.addActionListener(shapeChangeListener);
        ellipseButton.addActionListener(shapeChangeListener);
        crossButton.addActionListener(shapeChangeListener);

        GridLayout gridRowLayout = new GridLayout(1,0);
        JPanel shapeRadioPanel = new JPanel(gridRowLayout);

        JLabel shapeLabel = new JLabel("Shape:");

        shapeRadioPanel.add(rectangleButton);
        shapeRadioPanel.add(ellipseButton);
        shapeRadioPanel.add(crossButton);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 2;
        add(shapeLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        add(shapeRadioPanel, c);
    }

    private void setupImage() {
        imageView = new JLabel();

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        add(imageView, c);
    }
    
    private void processOperation() {
        Mat output = image.clone();
        if (erodeString.equals(currentOperation)) {
            output = controller.erode(image, kernelSize, currentShape);    
        } else if(dilateString.equals(currentOperation)) {
            output = controller.dilate(image, kernelSize, currentShape);
        } else if(openString.equals(currentOperation)) {
            output = controller.open(image, kernelSize, currentShape);
        } else if(closeString.equals(currentOperation)) {
            output = controller.close(image, kernelSize, currentShape);
        }
        updateView(output);
    }

    private void updateView(Mat mat) {
        imageView.setIcon(new ImageIcon(OpenCVUtilsJava.toBufferedImage(mat)));
    }
    

    class ExAMorphologicalOperationController {
        
        public Mat erode(Mat input, int elementSize, int elementShape) {
            Mat outputImage = new Mat();
            Mat element = getKernelFromShape(elementSize, elementShape);
            opencv_imgproc.erode(input, outputImage, element);
            return outputImage;
        }

        public Mat dilate(Mat input, int elementSize, int elementShape) {
            Mat outputImage = new Mat();
            Mat element = getKernelFromShape(elementSize, elementShape);
            opencv_imgproc.dilate(input, outputImage, element);
            return outputImage;
        }

        public Mat open(Mat input, int elementSize, int elementShape) {
            Mat outputImage = new Mat();
            Mat element = getKernelFromShape(elementSize, elementShape);
            opencv_imgproc.morphologyEx(input, outputImage, opencv_imgproc.MORPH_OPEN, element);
            return outputImage;
        }

        public Mat close(Mat input, int elementSize, int elementShape) {
            Mat outputImage = new Mat();
            Mat element = getKernelFromShape(elementSize, elementShape);
            opencv_imgproc.morphologyEx(input, outputImage, opencv_imgproc.MORPH_CLOSE, element);
            return outputImage;
        }
        
        private Mat getKernelFromShape(int elementSize, int elementShape) {
            return opencv_imgproc.getStructuringElement(elementShape, new Size(elementSize*2+1, elementSize*2+1), new Point(elementSize, elementSize) );
        }
    }

}
