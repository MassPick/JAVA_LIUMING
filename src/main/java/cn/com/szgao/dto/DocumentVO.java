package cn.com.szgao.dto;

/**
 * @author liuming
 *
 */
public class DocumentVO {
	
	public DocumentVO( ) {
		 
	}
	
	
	public DocumentVO(Object doc) {
		super();
		this.doc = doc;
	}

	private Object doc;

	public Object getDoc() {
		return doc;
	}

	public void setDoc(Object doc) {
		this.doc = doc;
	}
}
