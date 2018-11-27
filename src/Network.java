public class Network {

    // [warstwa][neuron]
    private double[][] output;
    //wagi dla [warstwa][neuron][wczesniejszy neuron]
    private double[][][] weights;
    //[warstwa][neuron]
    private double[][] bias;

    private double[][] error_signal;
    private double[][] output_derivative;

    // ILOSC WARSTW SIECI NEURONOWEJ
    private final int[] NETWORK_LAYER_SIZES;
    //Ilosc wejsc
    private final int INPUT_SIZE;
    //Ilosc wyjsc
    private final int OUTPUT_SIZE;
    //Rozmiar sieci
    private final int NETWORK_SIZE;

    public Network(int... NETWORK_LAYER_SIZES) {
        //Ilosc neurownow w poszczegolnych warstwach oraz ilosc warst
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
        // Ilosc wejsc odpowiada ilosci neurownow w warstwie pierwszej
        this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
        // Ilosc warstw w sieci
        this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;
        // ilosc neuronow w ostatniej warstwie
        this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[NETWORK_SIZE - 1];

        this.output = new double[NETWORK_SIZE][];
        this.weights = new double[NETWORK_SIZE][][];
        this.bias = new double[NETWORK_SIZE][];

        this.error_signal = new double[NETWORK_SIZE][];
        this.output_derivative = new double[NETWORK_SIZE][];

        for(int i = 0; i < NETWORK_SIZE; i++) {
            this.output[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.error_signal[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.output_derivative[i] = new double[NETWORK_LAYER_SIZES[i]];

            this.bias[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], 0.3, 0.7);

            if(i > 0) {
                weights[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], NETWORK_LAYER_SIZES[i - 1], -0.5, 0.3);
            }
        }
   }

   public double[] calculate(double... input) {
        //Jeśli ilosc weisc nie rowna sie ilosci neuronow w pierwszej warstwie to null
        if(input.length != this.INPUT_SIZE) return null;
        //wartosci w warstwie pierwszej sa rowne wejsciu
        this.output[0] = input;
        // dla kazdej warstwy w sieci
        for(int layer = 1; layer < NETWORK_SIZE; layer++) {
            //dla kazdego neuronu w warstwie
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = bias[layer][neuron];
                //dla neuronow w porzedniej warstwie
                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++) {
                    sum += output[layer - 1][prevNeuron] * weights[layer][neuron][prevNeuron];
                }
                output[layer][neuron] = sigmoid(sum);
                output_derivative[layer][neuron] = output[layer][neuron] * (1 - output[layer][neuron]);
            }
        }
        return output[NETWORK_SIZE - 1];
   }

   public void train(double[] input, double[] target, double eta) {
        if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) return;
        calculate(input);
        backPropagationError(target);
        updateWeights(eta);
   }

   public void backPropagationError(double[] target) {
        for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[NETWORK_SIZE - 1]; neuron++) {
            error_signal[NETWORK_SIZE -1][neuron] = (output[NETWORK_SIZE - 1][neuron] - target[neuron]) * output_derivative[NETWORK_SIZE - 1][neuron];
        }
        for(int layer = NETWORK_SIZE - 2; layer > 0; layer--) {
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = 0;
                for(int nextNeuron = 0; nextNeuron < NETWORK_LAYER_SIZES[layer + 1]; nextNeuron++) {
                    sum += weights[layer + 1][nextNeuron][neuron] * error_signal[layer + 1][nextNeuron];
                }
                this.error_signal[layer][neuron] = sum * output_derivative[layer][neuron];
            }
       }
   }

   public void updateWeights(double eta) {
        for(int layer = 1; layer < NETWORK_SIZE; layer++) {
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++) {
                    //weights[layer][neuron][prevNeuron]
                    double delta = - eta * output[layer - 1][prevNeuron] * error_signal[layer][neuron];
                    weights[layer][neuron][prevNeuron] += delta;
                }
                double delta = - eta * error_signal[layer][neuron];
                bias[layer][neuron] += delta;
            }
        }
   }

   private double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
   }

}
