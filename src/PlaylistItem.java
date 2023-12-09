//Fairly simple object that stores the ID and title of a video.

package localPlaylist;

public class PlaylistItem {
	public String ID;
	public String title;
	
	public PlaylistItem() {
		ID = "";
		title = "";
	}
	
	public PlaylistItem(String i, String t) {
		ID = i;
		title = t;
	}
	
	public String toHTML() {
		return "<li><a href=\"https://www.youtube.com/watch?v="+ID+"\"><span><img width=\"160\" height=\"90\" src=\"https://img.youtube.com/vi/"+ID+"/mqdefault.jpg\"></span>"+title+"</a></li>";
	}
	
	public String toString() {
		return ID+"\n"+title;
	}
}
