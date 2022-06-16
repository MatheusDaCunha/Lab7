class RecursoCompartrilhado {
	private int numeroDeThreads;
	private int numeroDeElementos;
	private int vetorDeInteiros[];
	private int quantidadeDePares;
	
	/* Construtor da classe RecursoCompartrilhado */
	public RecursoCompartrilhado () {
		this.numeroDeThreads = 4;
		this.numeroDeElementos = 1000000;
		this.vetorDeInteiros = new int[numeroDeElementos];
		this.inicializarVetor();
		this.quantidadeDePares = 0;
	}

	public int getNumeroDeThreads () {
		return this.numeroDeThreads;
	}

	public int getNumeroDeElementos () {
		return this.numeroDeElementos;
	}

	public int getElementoDoVetor (int indice) {
		return this.vetorDeInteiros[indice];
	}

	private void inicializarVetor () {
		for (int i = 0; i < this.numeroDeElementos; i++) {
			this.vetorDeInteiros[i] = i;
		}
	}

	public synchronized int getQuantidadeDePares () {
		return this.quantidadeDePares;
	}

	public synchronized void incrementarPares () {
		this.quantidadeDePares++;
	}
}

class TarefaDasThreads implements Runnable {
	private int tamanhoDoBloco, inicio, fim, id;
	RecursoCompartrilhado recurso;
	
	public TarefaDasThreads(int id, RecursoCompartrilhado recurso){
		this.id = id;
		this.tamanhoDoBloco = recurso.getNumeroDeElementos()/ recurso.getNumeroDeThreads();
		this.inicio = this.id * this.tamanhoDoBloco;
		
		if (this.id == recurso.getNumeroDeThreads() - 1)
			this.fim = recurso.getNumeroDeElementos();
		else
			this.fim = this.inicio + this.tamanhoDoBloco;
		
		this.recurso = recurso;
	}
	
	public void run() {		
		System.out.println("Thread " + id + "  - iniciou o processamento");
		for(int i = this.inicio; i < this.fim; i++) {
			if (recurso.getElementoDoVetor(i) % 2 == 0)
				this.recurso.incrementarPares();
		}
		System.out.println("Thread " + id + "  - finalizou o processamento");
	}
}

class EncontraPares {
	
	public static void main(String[] args) {
		RecursoCompartrilhado recurso = new RecursoCompartrilhado();
		Thread[] threads = new Thread[recurso.getNumeroDeThreads()];

		/* Cria as threads usando a interface Runnable */ 
		for (int i  = 0; i < recurso.getNumeroDeThreads(); i++) {
			threads[i] = new Thread(new TarefaDasThreads(i, recurso));
		}

		/* Inicia a execução de cada thread */
		for (int i  = 0; i < recurso.getNumeroDeThreads(); i++) {
			threads[i].start();
		}

		/* Espera o término de todas as threads */
		for (int i  = 0; i < recurso.getNumeroDeThreads(); i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {
				return;
			}
		}

		/* Exibe o total de números pares encontrados no vetor */
		System.out.println("Total de pares é " + recurso.getQuantidadeDePares());
	}
}




















