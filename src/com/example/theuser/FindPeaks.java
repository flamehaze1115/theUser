package com.example.theuser;


public class FindPeaks {
	
	Float[] peaks;
	Integer[] peakkeys;
	float max1,max2;
	int maxplace1,maxplace2;
	int len;
	
	int place1,place2;
	
	public FindPeaks(Float[] peaks,Integer[] peakkeys,float max,int maxplace)
	{
		this.FindPeaksGo(peaks, peakkeys,max,maxplace);
	}
	//�����ڻ���ؾ�����������ֵ��λ��
	public int getpeakkey1()
	{
		return this.peakkeys[place1];
	}
	
	public int getpeakkey2()
	{
		return this.peakkeys[place2];
	}
	//�����ڻ���ؾ���������ֵ�ķ�ֵ��С
	public float getpeak1()
	{
//		return this.peaks[place1];
		return this.max1;
	}
	
	public float getpeak2()
	{
		return this.peaks[place2];
	}
	
	private void FindPeaksGo(Float[] peaks,Integer[] peakkeys,float max,int maxplace)
	{
		this.peaks = peaks;
		this.peakkeys = peakkeys;
		this.max1 = max;
		this.maxplace1 = maxplace;
		
		this.len = this.peaks.length;
		
		for(int i=0;i<this.len;i++)
		{
			if(this.peaks[i]==this.max1)
			{
				place1 = i;//�ҵ����ֵ�ڷ�ֵ�����е�λ��
				break;
			}
		}
		//�����ֵǰ��400����ֵ��0
		for(int i=0;i<700;i++)
		{
			if((this.place1+i)<this.len)
			{
				this.peaks[this.place1+i] = 0f;
			}			
			if((this.place1-i)>0)
			{
				this.peaks[this.place1-i] = 0f;
			}	
		}
			
		this.max2 = 0;//�����ֵ�ĳ�ֵ��Ϊ0
		for(int i=0;i<len;i++)
		{
			if(this.peaks[i]>this.max2)
			{
				this.max2 = this.peaks[i];
				this.place2 = i;
			}
		}
//		
//		System.out.println(place1);
//		System.out.println(place2);
	}

}
