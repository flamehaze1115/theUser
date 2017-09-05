package com.example.theuser;

public class GenerateNumber {
	
	double result;
	double[] array;
	
	public GenerateNumber(double []array)
	{
		this.array = array;
		generate(this.array);
	}
	//������λ��
	public double GetResult()
	{
		return result;
	}
	
	private void generate(double []array) {
		int n = array.length;
		
        // ��������     
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < i; j++) {
                if (array[j] < array[i]) {
                    double temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }

        // ������λ��
        if (n % 2 == 0) {
            result = (array[n / 2 - 1] + array[n /2]) / 2;
        } else {
            result = array[n / 2];
        }
    }
}


