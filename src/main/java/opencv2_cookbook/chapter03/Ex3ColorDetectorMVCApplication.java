package opencv2_cookbook.chapter03;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import opencv2_cookbook.OpenCVUtilsJava;

import org.bytedeco.opencv.opencv_core.Mat;

/**
 * Example for section "Using a Model-View-Controller architecture to design an application" in Chapter 3.
 * This object corresponds to the MainWindow class in the C++ code.
 */
public class Ex3ColorDetectorMVCApplication {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Ex3ColorDetectorMVCFrame frame = new Ex3ColorDetectorMVCFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

class Ex3ColorDetectorMVCFrame extends JFrame {
    public Ex3ColorDetectorMVCFrame() {
        setTitle("Color Detector MVC");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        imageView = new JLabel();
        colorDistanceLabel = new JLabel("Color Distance Threshold: ???");
        colorDistanceLabel.setHorizontalAlignment(SwingConstants.LEADING);

        openImageButton = new JButton("Open Image");
        processImageButton = new JButton("Process Image");
        processImageButton.setEnabled(false);

        selectColorButton = new JButton("Select Color");
        colorDistanceSlider = new JSlider(0, 3 * 255);

        // Create vertical buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.add(openImageButton, new GBC(0, 0).setFill(GBC.HORIZONTAL).setWeight(0.5, 0).setInsets(5));
        buttonsPanel.add(new JSeparator(), new GBC(0, 1).setFill(GBC.HORIZONTAL).setWeight(0.5, 0).setInsets(5));
        buttonsPanel.add(selectColorButton, new GBC(0, 2).setFill(GBC.HORIZONTAL).setWeight(0.5, 0).setInsets(5));
        buttonsPanel.add(colorDistanceLabel, new GBC(0, 3).setFill(GBC.HORIZONTAL).setWeight(0.5, 0).setInsets(5));
        buttonsPanel.add(colorDistanceSlider, new GBC(0, 4).setFill(GBC.HORIZONTAL).setWeight(0.5, 0).setInsets(5));
        buttonsPanel.add(new JSeparator(), new GBC(0, 5).setFill(GBC.HORIZONTAL).setWeight(0.5, 0).setInsets(5));
        buttonsPanel.add(processImageButton, new GBC(0, 6).setFill(GBC.HORIZONTAL).setWeight(0.5, 0).setInsets(5));

        JScrollPane imageScrollPane = new JScrollPane(imageView);
        imageScrollPane.setPreferredSize(new Dimension(500, 400));

        JPanel contentsPanel = new JPanel();
        contentsPanel.setLayout(new FlowLayout());
        contentsPanel.add(buttonsPanel, FlowLayout.LEFT);
        contentsPanel.add(imageScrollPane, FlowLayout.CENTER);

        add(contentsPanel);

        // Listener
        openImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.onOpenImage();
            }
        });
        selectColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.onSelectColor();
            }
        });
        processImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.onProcessImage();
            }
        });
        colorDistanceSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.onColorDistanceSliderChange();
            }
        });

        // Sync display for the first time
        controller.onColorDistanceSliderChange();
    }

    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;

    private JButton openImageButton;
    private JButton processImageButton;
    private JButton selectColorButton;
    private JSlider colorDistanceSlider;
    private JLabel imageView;
    private JLabel colorDistanceLabel;
    private Ex3ColorDetectorMVCController controller = new Ex3ColorDetectorMVCController();;

    class Ex3ColorDetectorMVCController {
        
        private JFileChooser fileChooser = new JFileChooser(new File("./data"));
        private ColorDetectorController colorDetectorController = new ColorDetectorController();

        /**
         * Ask user for location and open new image.
         */
        private void onOpenImage() {
            if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
              return;
            }

            // Load the image
            File path = fileChooser.getSelectedFile().getAbsoluteFile();

            // Load image and update display.
            if (colorDetectorController.setInputImage(path.getAbsolutePath())) {
                display(colorDetectorController.inputImage);
                processImageButton.setEnabled(true);
            } else {
              JOptionPane.showMessageDialog(null, "Cannot open image file: " + path, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void onSelectColor() {
            Color color = JColorChooser.showDialog(null, "Select Target Color", colorDetectorController.getTargetColor().getColor());
            if (color != null) {
                colorDetectorController.setTargetColor(new ColorRGB(color));
            }
        }

        private void onProcessImage() {
            if (colorDetectorController.inputImage.empty()) {
                return;
            }
            colorDetectorController.process();
            display(colorDetectorController.result);
        }

        public void onColorDistanceSliderChange() {
          int value = colorDistanceSlider.getValue();
          colorDetectorController.setColorDistanceThreshold(value);
          colorDistanceLabel.setText("Color Distance Threshold: " + value);
        }
        
        private void display(Mat image) {
            imageView.setIcon(new ImageIcon(OpenCVUtilsJava.toBufferedImage(image)));
        }
    }

}
