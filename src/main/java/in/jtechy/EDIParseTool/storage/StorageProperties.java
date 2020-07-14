package in.jtechy.EDIParseTool.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("storage")
@Component
public class StorageProperties {

	/**
	 * Folder location for storing files
	 */

	private String ediIbUploadLocation = "edi-ib-upload-dir";
	private String ediObUploadLocation = "edi-ob-upload-dir";

	public String getEdiIbUploadLocation() {
		return ediIbUploadLocation;
	}

	public void setEdiIbUploadLocation(String ediIbUploadLocation) {
		this.ediIbUploadLocation = ediIbUploadLocation;
	}

	public String getEdiObUploadLocation() {
		return ediObUploadLocation;
	}

	public void setEdiObUploadLocation(String ediObUploadLocation) {
		this.ediObUploadLocation = ediObUploadLocation;
	}

	private String downLoadLocation = "download-dir";

	public String getDownLoadLocation() {
		return downLoadLocation;
	}

	public void setDownLoadLocation(String downLoadLocation) {
		this.downLoadLocation = downLoadLocation;
	}
}
