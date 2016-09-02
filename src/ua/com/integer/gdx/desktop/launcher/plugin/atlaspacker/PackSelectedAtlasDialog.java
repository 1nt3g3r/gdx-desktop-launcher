package ua.com.integer.gdx.desktop.launcher.plugin.atlaspacker;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import ua.com.integer.gdx.desktop.launcher.Util;

public class PackSelectedAtlasDialog extends JDialog {
	private static final long serialVersionUID = -5641715308311371452L;
	
	private AtlasPackerPlugin atlasPacker;
	private JList<String> atlasNamesList;
	
	public PackSelectedAtlasDialog(AtlasPackerPlugin atlasPacker) {
		this.atlasPacker = atlasPacker;
		
		setTitle("Atlases");
		setSize(400, 800);
		Util.situateOnCenter(this);
		
		setLayout(new BorderLayout());
		addAtlasNames();
		addPackButton();
	}
	
	private void addAtlasNames() {
		atlasNamesList = new JList<>(atlasPacker.getAtlasNames());
		atlasNamesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scroll = new JScrollPane(atlasNamesList);
		add(scroll, BorderLayout.CENTER);
	}
	
	private void addPackButton() {
		JButton packButton = new JButton("Pack selected atlases");
		packButton.addActionListener(new PackSelectedAtlasesListener());
		add(packButton, BorderLayout.PAGE_END);
	}
	
	class PackSelectedAtlasesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for(int index : atlasNamesList.getSelectedIndices()) {
				atlasPacker.packAtlas(atlasNamesList.getModel().getElementAt(index));
			}
			JOptionPane.showMessageDialog(null, "Selected atlases packed");
		}
	}
}
