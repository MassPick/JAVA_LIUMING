package cn.com.szgao.dto;
import java.util.List;


/**
 * EnterpriseVO类传集合问题
 * @author dell
 *
 */
public class EnterpriseListVO {

    /**
     * EnterpriseVO集合
     */
	private List<EnterpriseVO> doc;
	private ParameterVO parameter;
	public List<EnterpriseVO> getDoc() {
		return doc;
	}

	public void setDoc(List<EnterpriseVO> doc) {
		this.doc = doc;
	}

	public ParameterVO getParameter() {
		return parameter;
	}

	public void setParameter(ParameterVO parameter) {
		this.parameter = parameter;
	}
	
}
