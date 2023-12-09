package localPlaylist;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

public class HelpWindow extends Dialog {

	protected Object result;
	protected Shell shlHelp;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public HelpWindow(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlHelp.open();
		shlHelp.layout();
		Display display = getParent().getDisplay();
		while (!shlHelp.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlHelp = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlHelp.setSize(530, 350);
		shlHelp.setText("What all the buttons do:");
		
		Button btnOK = new Button(shlHelp, SWT.NONE);
		btnOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlHelp.dispose();
			}
		});
		btnOK.setBounds(227, 280, 70, 25);
		btnOK.setText("OK");
		
		Label lblHelpTextLabel = new Label(shlHelp, SWT.NONE);
		lblHelpTextLabel.setBounds(10, 10, 504, 264);
		lblHelpTextLabel.setText("Display: Convert the playlist to a temporary HTML file and display it in your default browser.\r\n\r\n\u25B2: Move the currently selected item up in the playlist.\r\n\r\n\u25BC: Move the currently selected item down in the playlist.\r\n\r\n+: Add a new item to the top of the list. You can just copy and paste the URL into the popup.\r\n\r\n-: Remove the currently selected item from the playlist.\r\n\r\nLoad: Open a previously created playlist file.\r\n\r\nSave: Save the current playlist file. If not already working with an existing file, same as Save As.\r\n\r\nSave As: Save the current playlist as a file with the location and filename of your choice.\r\n\r\nHelp: Open this window.");

	}
}
