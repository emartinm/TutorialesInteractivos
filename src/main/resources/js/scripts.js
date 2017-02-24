function capturaEnlaces() {
    elems = document.getElementsByTagName("a");
	for(var i = 0; i < elems.length; ++i){
		elems[i].onclick=function(){
			this.innerHTML="Estoy trasteando para abrir los enlaces en el navegador del sistema";
			//bridge.hello();
			//bridge.show(this.href);
			bridge.openWebPage(this.href);
			return false;
		}
	}
}