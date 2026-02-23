public int CalcularCosteEjecucionTarea() {
	int tokensTarea = ((StringTokenizer)this.prompt).countTokens;
	LLM llmAux = this.LLM;
	int costeToken = llmAux.getCostePorToken();
	int costeTarea = costeToken * tokensTarea;

	int costeRespuestas = 0;

	for (Respuesta r: this.getRespuestas) {
		costeRespuestas += r.CalcularCosteRespuestas();
	}

	return costeRespuestas + costeTarea;	
}

public int CalcularCosteRespuesta() {
	int tokensTarea = ((StringTokenizer)this.respuesta).countTokens;
	LLM llmAux = this.LLM;
	int costeToken = llmAux.getCostePorToken();

	return costeToken * tokensTarea;
}