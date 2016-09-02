package ua.com.integer.gdx.desktop.launcher.plugin.atlaspacker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class AtlasPropertiesPanel extends JPanel {
	private static final long serialVersionUID = -6391694649594852757L;
	
	public static final String[] TEXTURE_FILTERS = new String[] {
			"Nearest", "Linear", "MipMap", "MipMapNearestNearest", "MipMapLinearNearest",
			"MipMapNearestLinear", "MipMapLinearLinear"
	};
	
	public static final String[] TEXTURE_WRAP = new String[] {
			"MirroredRepeat", "ClampToEdge", "Repeat"
	};
	
	public static final String[] TEXTURE_FORMAT = new String[] {
			"Alpha", "Intensity", "LuminanceAlpha", "RGB565", "RGBA4444", "RGB888", "RGBA8888"
	};
	
	private JPanel content;
	
	private JCheckBox pot, bleed, edgePadding, duplicatePadding, rotation, square,
					  stripWhiteSpaceX, stripWhiteSpaceY, alias, ignoreBlankImages,
					  fast, debug, combineSubdirectories, flattenPaths, premultiplyAlpha,
					  useIndexes, limitMemory, grid;
	
	private JSpinner paddingX, paddingY, minWidth, minHeight, maxWidth, maxHeight,
					 alphaTreshold, jpegQuality;
	
	private JComboBox<String> filterMin, filterMax, wrapX, wrapY, format, outputFormat;

	public AtlasPropertiesPanel() {
		setLayout(new BorderLayout());
		
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		JScrollPane scroll = new JScrollPane(content);
		add(scroll, BorderLayout.CENTER);
		
		title("OUTPUT FORMAT");
		outputFormat = addComboboxControl("File Format:", "png", "jpg");
		format = addComboboxControl("Format:", TEXTURE_FORMAT);
		jpegQuality = addSpinnerControl("JPEG Quality:", false);
		filterMin = addComboboxControl("Filter Min:", TEXTURE_FILTERS);
		filterMax = addComboboxControl("Filter Max:", TEXTURE_FILTERS);
		wrapX = addComboboxControl("Wrap X:", TEXTURE_WRAP);
		wrapY = addComboboxControl("Wrap Y:", TEXTURE_WRAP);
		premultiplyAlpha = addCheckboxControl("Premultiply Alpha");
		alphaTreshold = addSpinnerControl("Alpha Treshold", false);
		
		title("SIZE");
		minWidth = addSpinnerControl("Min Width:", true);
		minHeight = addSpinnerControl("Min Height:", true);
		maxWidth = addSpinnerControl("Max Width:", true);
		maxHeight = addSpinnerControl("Max Height:", true);

		title("CONSTRAINTS");
		pot = addCheckboxControl("Power Of Two");
		square = addCheckboxControl("Square");
		rotation = addCheckboxControl("Rotation");
		
		title("PADDING");
		paddingX = addSpinnerControl("Padding X:", true);
		paddingY = addSpinnerControl("Padding Y:", true);
		edgePadding = addCheckboxControl("Edge Padding:");
		duplicatePadding = addCheckboxControl("Duplicate Padding");
		
		title("PROCESS DETAILS");
		combineSubdirectories = addCheckboxControl("Combine Subdirectories");
		flattenPaths = addCheckboxControl("Flatten Paths");

		title("PERFORMANCE");
		limitMemory = addCheckboxControl("Limit Memory");
		fast = addCheckboxControl("Fast");
		
		title("OTHER");
		debug = addCheckboxControl("Debug");
		grid = addCheckboxControl("Grid");
		useIndexes = addCheckboxControl("Use Indexes");
		bleed = addCheckboxControl("Bleed");
		stripWhiteSpaceX = addCheckboxControl("Strip Whitespace X");
		stripWhiteSpaceY = addCheckboxControl("Strip Whitespace Y");
		alias = addCheckboxControl("Alias");
		ignoreBlankImages = addCheckboxControl("Ignore Blank Images");
		
		content.add(Box.createVerticalGlue());
	}
	
	private JCheckBox addCheckboxControl(String name) {
		JCheckBox result = new JCheckBox(name);
		wrap(result);
		return result;
	}
	
	private JPanel wrap(JComponent ... components) {
		JPanel wrapper = new JPanel();
		wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.LINE_AXIS));
		
		for(JComponent component : components) {
			wrapper.add(component);
			wrapper.add(Box.createRigidArea(new Dimension(5, 5)));
		}
		wrapper.add(javax.swing.Box.createHorizontalGlue());
		
		content.add(wrapper);
		content.add(Box.createRigidArea(new Dimension(5, 5)));
		
		return wrapper;
	}
	
	private JSpinner addSpinnerControl(String name, boolean integer) {
		SpinnerNumberModel spinnerModel = null;
		if (integer) {
			spinnerModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
		} else {
			spinnerModel = new SpinnerNumberModel(0f, 0f, Float.MAX_VALUE, 0.1f);
		}
		
		JSpinner result = new JSpinner(spinnerModel);
		
		wrap(size(new JLabel(name), 150, 20), size(result, 50, 20));
		
		return result;
	}
	
	private JComboBox<String> addComboboxControl(String name, String ... variants) {
		JComboBox<String> result = new JComboBox<String>(variants);
		
		wrap(size(new JLabel(name), 150, 20), size(result, 200, 25));
		
		return result;
	}
	
	private JLabel title(String name) {
		JLabel result = new JLabel(name);
		result.setForeground(Color.BLUE);
		wrap(size(result, 200, 20));
		return result;
	}
	
	private JComponent size(JComponent component, int width, int height) {
		Dimension d = new Dimension(width, height);
		component.setPreferredSize(d);
		component.setMaximumSize(d);
		component.setMinimumSize(d);
		component.setSize(d);
		return component;
	}
	
	public static void main(String[] args) {
		JFrame frm = new JFrame("Atlas properties");
		frm.setContentPane(new AtlasPropertiesPanel());
		frm.setSize(400, 600);
		frm.setVisible(true);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
