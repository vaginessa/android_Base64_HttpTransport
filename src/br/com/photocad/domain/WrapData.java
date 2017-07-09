package br.com.photocad.domain;


public class WrapData {
	private String url;
	private String method;
	private String name;
	private String telefone;
	private String descricao;
	private Image image;
	
	
	public WrapData(){
		this.image = new Image();
	}
	public WrapData(String url, String method, String name, String telefone,String descricao, Image image){
		this.url = url;
		this.method = method;
		this.name = name;
		this.telefone = telefone;
		this.descricao = descricao;
		this.image = image;
	}

	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}


	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
}
