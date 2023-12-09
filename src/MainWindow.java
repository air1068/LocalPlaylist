package localPlaylist;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MainWindow {
	static ArrayList<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
	static String playlistFileLocation = "";

	protected Shell shlLocalPlaylist;

	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			loadPlaylist(new File(args[0]));
			runPage();
			return;
		}
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlLocalPlaylist.open();
		shlLocalPlaylist.layout();
		while (!shlLocalPlaylist.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shlLocalPlaylist = new Shell(SWT.CLOSE | SWT.MIN | SWT.TITLE);
		shlLocalPlaylist.setSize(543, 310);
		shlLocalPlaylist.setText("LocalPlaylist");
		
		ListViewer listViewer = new ListViewer(shlLocalPlaylist, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		list.setBounds(10, 11, 448, 250);
		
		Button btnUp = new Button(shlLocalPlaylist, SWT.NONE);
		btnUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i = list.getSelectionIndex();
				if (i > 0) {
					String tmp = list.getItem(i);
					list.setItem(i, list.getItem(i-1));
					list.setItem(i-1, tmp);
					list.select(i-1);
					Collections.swap(playlist, i, i-1);
				}
			}
		});
		btnUp.setToolTipText("Move selected entry up");
		btnUp.setBounds(464, 41, 25, 25);
		btnUp.setText("\u25B2");
		
		Button btnAdd = new Button(shlLocalPlaylist, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EnterURL u = new EnterURL(shlLocalPlaylist, SWT.APPLICATION_MODAL);
				u.open();
				if (u.result != null) {
					btnAdd.setEnabled(false);
					playlist.add(0, parseYouTubeURL(u.result.toString()));
					list.add(playlist.get(0).title, 0);
					btnAdd.setEnabled(true);
				}
			}
		});
		btnAdd.setToolTipText("Add new entry");
		btnAdd.setBounds(495, 41, 25, 25);
		btnAdd.setText("+");
		
		Button btnRemove = new Button(shlLocalPlaylist, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i = list.getSelectionIndex();
				if (i > -1) {
					list.remove(i);
					playlist.remove(i);
				}
			}
		});
		btnRemove.setToolTipText("Delete selected entry");
		btnRemove.setBounds(495, 72, 25, 25);
		btnRemove.setText("-");
		
		Button btnDown = new Button(shlLocalPlaylist, SWT.NONE);
		btnDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i = list.getSelectionIndex();
				if ((i < list.getItemCount()-1) && (i > -1)) {
					String tmp = list.getItem(i);
					list.setItem(i, list.getItem(i+1));
					list.setItem(i+1, tmp);
					list.select(i+1);
					Collections.swap(playlist, i, i+1);
				}
			}
		});
		btnDown.setToolTipText("Move selected entry down");
		btnDown.setBounds(464, 72, 25, 25);
		btnDown.setText("\u25BC");
		
		Button btnSave = new Button(shlLocalPlaylist, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (playlistFileLocation == null || playlistFileLocation.isEmpty()) {
					FileDialog dialog = new FileDialog(shlLocalPlaylist, SWT.SAVE);
					dialog.setFilterExtensions(new String [] {"*.lpl", "*.*"});
					dialog.setFilterPath(System.getProperty("user.home") + File.separator +"Desktop");
					playlistFileLocation = dialog.open();
				}
				if (playlistFileLocation != null) {
					if (!playlistFileLocation.endsWith(".lpl")) {
						playlistFileLocation += ".lpl";
					}
					writePlaylist(new File(playlistFileLocation));
					shlLocalPlaylist.setText("LocalPlaylist - "+playlistFileLocation);
				}
			}
		});
		btnSave.setBounds(464, 134, 56, 25);
		btnSave.setText("Save");
		
		Button btnSaveAs = new Button(shlLocalPlaylist, SWT.NONE);
		btnSaveAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shlLocalPlaylist, SWT.SAVE);
				dialog.setFilterExtensions(new String [] {"*.lpl"});
				dialog.setFilterPath(System.getProperty("user.home") + File.separator +"Desktop");
				playlistFileLocation = dialog.open();
				if (playlistFileLocation != null) {
					if (!playlistFileLocation.endsWith(".lpl")) {
						playlistFileLocation += ".lpl";
					}
					writePlaylist(new File(playlistFileLocation));
					shlLocalPlaylist.setText("LocalPlaylist - "+playlistFileLocation);
				}
			}
		});
		btnSaveAs.setBounds(464, 165, 56, 25);
		btnSaveAs.setText("Save As");
		
		Button btnLoad = new Button(shlLocalPlaylist, SWT.NONE);
		btnLoad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shlLocalPlaylist, SWT.OPEN);
				dialog.setFilterExtensions(new String [] {"*.lpl"});
				dialog.setFilterPath(System.getProperty("user.home") + File.separator +"Desktop");
				playlistFileLocation = dialog.open();
				if (playlistFileLocation != null) {
					File lpl = new File(playlistFileLocation);
					loadPlaylist(lpl);
					list.removeAll(); //clear the display list (since the actual list was replaced with the one from the file)
					for (PlaylistItem p : playlist) {
						list.add(p.title); //repopulate the display list
					}
					shlLocalPlaylist.setText("LocalPlaylist - "+playlistFileLocation);
				}
			}
		});
		btnLoad.setBounds(464, 103, 56, 25);
		btnLoad.setText("Load");
		
		Button btnHelp = new Button(shlLocalPlaylist, SWT.NONE);
		btnHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HelpWindow helpPopup = new HelpWindow(shlLocalPlaylist, SWT.APPLICATION_MODAL);
				helpPopup.open();
			}
		});
		btnHelp.setBounds(464, 196, 56, 25);
		btnHelp.setText("Help");
		
		Button btnDisplay = new Button(shlLocalPlaylist, SWT.NONE);
		btnDisplay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runPage();
			}
		});
		btnDisplay.setBounds(464, 10, 56, 25);
		btnDisplay.setText("Display");
	}
	
	public PlaylistItem parseYouTubeURL(String URLstring) {
		PlaylistItem r = new PlaylistItem();
		try {
			URL url = new URL(URLstring);
			//extract video ID from URL
			if (url.getHost().equals("www.youtube.com") || url.getHost().equals("youtube.com")) {
				String[] parameters = url.getQuery().split("&");
				for (int i = 0; i < parameters.length; i++) {
					if (parameters[i].startsWith("v=")) {
						r.ID = parameters[i].substring(2); //remove the "v="
						break;
					}
				}
			} else if (url.getHost().equals("youtu.be")) {
				r.ID = url.getPath().substring(1);  //remove the initial "/"
			} else {
				return r;
			}
			//fetch page and extract title
	        HTMLEditorKit htmlKit = new HTMLEditorKit();
	        HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
	        HTMLEditorKit.Parser parser = new ParserDelegator();
	        parser.parse(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8), htmlDoc.getReader(0), true);
	        r.title = htmlDoc.getProperty("title").toString();
	        r.title = r.title.substring(0, r.title.length() - 10); //remove " - YouTube"
		} catch (MalformedURLException e) {
			errorMessage(e);
		} catch (IOException e) {
			errorMessage(e);
		}
		return r;
	}
	
	public static void runPage() {
		String page = """
<!DOCTYPE html>
<html>
<head>
<title>Local Playlist</title>
<style>
	li > a > span {
		display:inline-block; vertical-align:middle; margin-right:20px;
	}
	body {
		background-color: #444444;
		color: whitesmoke;
		font-family: "Roboto", "Arial", sans-serif;
		font-size: 1rem;
		font-weight: bold;
	}
	a:link, a:visited {
		color: whitesmoke;
		text-decoration: none;
	}
</style>
</head>
<body>
<p><ul>
		""";
		for (PlaylistItem p : playlist) {
			page += p.toHTML();
		}
		page += "</ul></p></body></html>";
        try {
			File tmpfile = File.createTempFile("playlist", ".html");
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(tmpfile.getAbsolutePath()), StandardCharsets.UTF_8);
			out.write(page);
			out.close();
			Desktop.getDesktop().open(tmpfile);
		} catch (IOException e) {
			errorMessage(e);
		}
	}
	
	public static void loadPlaylist(File lpl) {
		playlist.clear();
		try {
			java.util.List<String> lines = Files.readAllLines(lpl.toPath());
			for (int i = 0; i < lines.size(); i += 2) {
				playlist.add(new PlaylistItem(lines.get(i), lines.get(i+1)));
			}
		} catch (IOException e) {
			errorMessage(e);
		}
	}
	
	public void writePlaylist(File lpl) {
		String r = "";
		if (playlist.size() > 0) { 
			r = playlist.get(0).toString();
			for (int i = 1; i < playlist.size(); i++) {
				r += "\n"+playlist.get(i).toString();
			}
		}
		try {
			Files.writeString(lpl.toPath(), r);
		} catch (IOException e) {
			errorMessage(e);
		}
	}
	
	public static void errorMessage(Exception e) {
		MessageBox iamerror = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
		iamerror.setText(e.getClass().toString());
		iamerror.setMessage(e.getMessage());
		iamerror.open();
	}
}
