package localPlaylist;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class EnterURL extends Dialog {

	protected Object result;
	protected Shell shlEnterUrl;
	private Text text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EnterURL(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlEnterUrl.open();
		shlEnterUrl.layout();
		Display display = getParent().getDisplay();
		while (!shlEnterUrl.isDisposed()) {
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
		shlEnterUrl = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlEnterUrl.setSize(392, 85);
		shlEnterUrl.setText("Enter URL");
		
		text = new Text(shlEnterUrl, SWT.BORDER);
		text.setBounds(10, 10, 306, 21);
		
		Button btnOk = new Button(shlEnterUrl, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = text.getText();
				shlEnterUrl.dispose();
			}
		});
		btnOk.setBounds(322, 8, 40, 25);
		btnOk.setText("OK");
	}
}
