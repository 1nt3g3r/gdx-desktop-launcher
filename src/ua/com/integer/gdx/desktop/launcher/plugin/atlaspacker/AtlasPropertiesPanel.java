package ua.com.integer.gdx.desktop.launcher.plugin.atlaspacker;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import ua.com.integer.gdx.desktop.launcher.Util;

public class AtlasPropertiesPanel extends JDialog {
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
					 alphaTreshold, jpegQuality, scale;
	
	private JComboBox<String> filterMin, filterMag, wrapX, wrapY, format, outputFormat;

	private File atlasFolder;
	private TexturePacker.Settings settings;

	private AtlasPackerPlugin atlasPacker;

	public AtlasPropertiesPanel(AtlasPackerPlugin atlasPacker, File atlasFolder) {
		this.atlasFolder = atlasFolder;
		this.atlasPacker = atlasPacker;

		setTitle("<" + atlasFolder.getName() + "> Atlas Properties");

		setLayout(new BorderLayout());
		
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		JScrollPane scroll = new JScrollPane(content);
		add(scroll, BorderLayout.CENTER);
		
		title("OUTPUT FORMAT");
		outputFormat = addComboboxControl("File Format:", "png", "jpg", "etc1");
		format = addComboboxControl("Format:", TEXTURE_FORMAT);
		jpegQuality = addSpinnerControl("JPEG Quality:", false);
		filterMin = addComboboxControl("Filter Min:", TEXTURE_FILTERS);
		filterMag = addComboboxControl("Filter Max:", TEXTURE_FILTERS);
		wrapX = addComboboxControl("Wrap X:", TEXTURE_WRAP);
		wrapY = addComboboxControl("Wrap Y:", TEXTURE_WRAP);
		premultiplyAlpha = addCheckboxControl("Premultiply Alpha");
		alphaTreshold = addSpinnerControl("Alpha Treshold", true);
		
		title("SIZE");
		minWidth = addSpinnerControl("Min Width:", true);
		minHeight = addSpinnerControl("Min Height:", true);
		maxWidth = addSpinnerControl("Max Width:", true);
		maxHeight = addSpinnerControl("Max Height:", true);
		scale = addSpinnerControl("Scale", false);

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

		addPackAtlasButton();

		content.add(Box.createVerticalGlue());

		loadAtlasSettings();

		setVisible(true);
		setSize(600, 600);
		Util.situateOnCenter(this);
		addWindowListener(new SettingsPanelCloseListener());
		setModal(true);
	}

	private void addPackAtlasButton() {
		JButton packAtlasButton = new JButton("Pack atlas");
		packAtlasButton.setBackground(Color.GREEN);
		packAtlasButton.addActionListener(new PackAtlasListener());
		wrap(size(packAtlasButton, 200, 25));
	}

	class PackAtlasListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			saveAtlasSettings();
			atlasPacker.packAtlas(atlasFolder.getName());
		}
	}

	private void loadAtlasSettings() {
		boolean needSaveSettings = false;
		if (isSettingsFileExists()) {
			try {
				settings = new Json().fromJson(TexturePacker.Settings.class, new FileInputStream(new File(atlasFolder, "pack.json")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			settings = new TexturePacker.Settings();
			needSaveSettings = true;
		}

		pot.setSelected(settings.pot);
		paddingX.setValue(settings.paddingX);
		paddingY.setValue(settings.paddingY);
		bleed.setSelected(settings.bleed);
		edgePadding.setSelected(settings.edgePadding);
		duplicatePadding.setSelected(settings.duplicatePadding);
		rotation.setSelected(settings.rotation);
		minWidth.setValue(settings.minWidth);
		minHeight.setValue(settings.minHeight);
		maxWidth.setValue(settings.maxWidth);
		maxHeight.setValue(settings.maxHeight);
		square.setSelected(settings.square);
		stripWhiteSpaceX.setSelected(settings.stripWhitespaceX);
		stripWhiteSpaceY.setSelected(settings.stripWhitespaceY);
		alphaTreshold.setValue(settings.alphaThreshold);
		filterMin.setSelectedItem(settings.filterMin.toString());
		filterMag.setSelectedItem(settings.filterMag.toString());
		wrapX.setSelectedItem(settings.wrapX.toString());
		wrapY.setSelectedItem(settings.wrapY.toString());
		format.setSelectedItem(settings.format.toString());
		alias.setSelected(settings.alias);
		outputFormat.setSelectedItem(settings.outputFormat);
		jpegQuality.setValue(settings.jpegQuality);
		ignoreBlankImages.setSelected(settings.ignoreBlankImages);
		fast.setSelected(settings.fast);
		debug.setSelected(settings.debug);
		combineSubdirectories.setSelected(settings.combineSubdirectories);
		flattenPaths.setSelected(settings.flattenPaths);
		premultiplyAlpha.setSelected(settings.premultiplyAlpha);
		useIndexes.setSelected(settings.useIndexes);
		limitMemory.setSelected(settings.limitMemory);
		grid.setSelected(settings.grid);
		scale.setValue(settings.scale[0]);

		if (needSaveSettings) {
			saveAtlasSettings();
		}
	}

	private boolean isSettingsFileExists() {
		return new File(atlasFolder, "pack.json").exists();
	}

	private void saveAtlasSettings() {
		settings.pot = pot.isSelected();
		settings.paddingX = (Integer) paddingX.getValue();
		settings.paddingY = (Integer) paddingY.getValue();
		settings.bleed = bleed.isSelected();
		settings.edgePadding = edgePadding.isSelected();
		settings.duplicatePadding = duplicatePadding.isSelected();
		settings.rotation = rotation.isSelected();
		settings.minWidth = (Integer) minWidth.getValue();
		settings.minHeight = (Integer) minHeight.getValue();
		settings.maxWidth = (Integer) maxWidth.getValue();
		settings.maxHeight = (Integer) maxHeight.getValue();
		settings.square = square.isSelected();
		settings.stripWhitespaceX = stripWhiteSpaceX.isSelected();
		settings.stripWhitespaceY = stripWhiteSpaceY.isSelected();
		settings.alphaThreshold = (Integer) alphaTreshold.getValue();
		settings.filterMin = Texture.TextureFilter.valueOf(filterMin.getSelectedItem().toString());
		settings.filterMag = Texture.TextureFilter.valueOf(filterMag.getSelectedItem().toString());
		settings.wrapX = Texture.TextureWrap.valueOf(wrapX.getSelectedItem().toString());
		settings.wrapY = Texture.TextureWrap.valueOf(wrapY.getSelectedItem().toString());
		settings.format = Pixmap.Format.valueOf(format.getSelectedItem().toString());
		settings.alias = alias.isSelected();
		settings.outputFormat = outputFormat.getSelectedItem().toString();
		settings.jpegQuality = Float.parseFloat (jpegQuality.getValue().toString());
		settings.ignoreBlankImages = ignoreBlankImages.isSelected();
		settings.fast = fast.isSelected();
		settings.debug = debug.isSelected();
		settings.combineSubdirectories = combineSubdirectories.isSelected();
		settings.flattenPaths = flattenPaths.isSelected();
		settings.premultiplyAlpha = premultiplyAlpha.isSelected();
		settings.useIndexes = useIndexes.isSelected();
		settings.limitMemory = limitMemory.isSelected();
		settings.grid = grid.isSelected();
		settings.scale[0] = Float.parseFloat(scale.getValue().toString());

		try {
			new Json().toJson(settings, new FileWriter(new File(atlasFolder, "pack.json")));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			wrapper.add(Box.createRigidArea(new Dimension(5, 1)));
		}
		wrapper.add(javax.swing.Box.createHorizontalGlue());
		
		content.add(wrapper);
		content.add(Box.createRigidArea(new Dimension(5, 1)));
		
		return wrapper;
	}
	
	private JSpinner addSpinnerControl(String name, boolean integer) {
		SpinnerNumberModel spinnerModel = null;
		if (integer) {
			spinnerModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
		} else {
			spinnerModel = new SpinnerNumberModel(0f, 0f, Float.MAX_VALUE, 0.01f);
		}
		
		JSpinner result = new JSpinner(spinnerModel);
		
		wrap(size(new JLabel(name), 150, 20), size(result, 100, 20));
		
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

	class SettingsPanelCloseListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			saveAtlasSettings();
			dispose();
		}
	}

	public static void main(String[] args) {
		new AtlasPropertiesPanel(null, new File("/home/integer"));
	}
}
