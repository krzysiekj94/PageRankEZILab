import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PageRank {
    
	private static int MATRIX_SIZE = 10;
	private static int ITERATIONS = 100;
    private static double q = 0.15;
    private double[][] M;

    public static void main(String[] args) {
        PageRank pageRank = new PageRank();
        pageRank.run();
    }


    public void run() {

        double[][] L = new double[10][];
        L[0] = new double[]{0, 1, 1, 0, 1, 0, 0, 0, 0, 0};
        L[1] = new double[]{1, 0, 0, 1, 0, 0, 0, 0, 0, 0};
        L[2] = new double[]{0, 1, 0, 0, 0, 0, 1, 0, 0, 0};
        L[2] = new double[]{0, 1, 0, 0, 0, 0, 1, 0, 0, 0};
        L[3] = new double[]{0, 1, 1, 0, 0, 0, 0, 0, 0, 0};
        L[4] = new double[]{0, 0, 0, 0, 0, 1, 1, 0, 0, 0};
        L[5] = new double[]{0, 0, 0, 0, 0, 0, 1, 1, 0, 0};
        L[6] = new double[]{0, 0, 0, 0, 1, 1, 1, 1, 1, 1};
        L[7] = new double[]{0, 0, 0, 0, 0, 0, 1, 0, 1, 0};
        L[8] = new double[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 1};
        L[9] = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        System.out.println("Matrix L (indices)");
        printMatrix(L);

        M = getM(L);
        System.out.println("Matrix M (indices)");
        printMatrix(M);

        System.out.println("PAGERANK");
        double[] pr = pageRank(q);
        sortAndPrint(pr);

        System.out.println("TRUSTRANK (DOCUMENTS 1 AND 2 ARE GOOD)");
        L[0][4] = 0;
        L[2][6] = 0;
        M = getM(L);

        sortAndPrint(trustRank(q));
    }

    private double[][] getM( double[][] L ) {
        //TODO 1: Compute stochastic matrix M
    	//DONE!
    	
        int[] c = GetNumbersOfOutgoingLinksMatrix( L );		// vector with number of outgoing links
        double[][] M = GetStochasticMatrix( L, c );  			// stochastic matrix

        return M;
    }

    private double[][] GetStochasticMatrix( double[][] L, int[] c ) {
    	double[][] M = GetInitializedMatrixM();
    	
        for( int iRowMatrixIndex = 0; iRowMatrixIndex < MATRIX_SIZE; iRowMatrixIndex++ )
        {
        	for( int iColumnMatrixIndex = 0; iColumnMatrixIndex < MATRIX_SIZE; iColumnMatrixIndex++ )
        	{
        		if( L[iColumnMatrixIndex][iRowMatrixIndex] != 0.0 )
        		{
        			M[iRowMatrixIndex][iColumnMatrixIndex] = ( 1.0 / (double) c[iColumnMatrixIndex] );
        		}
        	}
        }
    	
    	return M;
	}

	private int[] GetNumbersOfOutgoingLinksMatrix( double[][] L ){
    	int[] c = GetInitializedVectorC();	
    	
        for( int iVectorRowIndex = 0; iVectorRowIndex < MATRIX_SIZE; iVectorRowIndex++)
        {        	
        	for( int iVectorColumnIndex = 0; iVectorColumnIndex < MATRIX_SIZE; iVectorColumnIndex++ )
        	{
        		c[iVectorRowIndex] += L[iVectorRowIndex][iVectorColumnIndex]; 
        	}
        }
    	
    	return c;
	}

	private int[] GetInitializedVectorC(){
    	int[] c = new int[MATRIX_SIZE];
        
        for( int iVectorIndex = 0; iVectorIndex < MATRIX_SIZE; iVectorIndex++ )
        {
        	c[iVectorIndex] = 0;
        }
    	
    	return c;
	}

	private double[][] GetInitializedMatrixM(){
        double[][] M = new double[MATRIX_SIZE][MATRIX_SIZE];
        
        for( int iRowMatrixIndex = 0; iRowMatrixIndex < MATRIX_SIZE; iRowMatrixIndex++ )
        {
        	for( int iColumnMatrixIndex = 0; iColumnMatrixIndex < MATRIX_SIZE; iColumnMatrixIndex++ )
        	{
        		M[iRowMatrixIndex][iColumnMatrixIndex] = 0.0;
        	}
        }
        
        return M;
	}

	private double[] pageRank( double q ) {
        //TODO 2: compute PageRank with damping factor q (method parameter, by default q value from class constant)
        //return array of PageRank values (indexes: page number - 1, e.g. result[0] = TrustRank of page 1).
		//DONE!
		
		double[] adPageRankArray = new double[MATRIX_SIZE];
		int iLinkFromAnotherNodeCount = 0;
		double dPageRankPreviousValue = 0.0;
		double dSumOfPageRank = 0.0;
		ArrayList<Integer> dRememberIndexAnotherLinkArrayList = new ArrayList<Integer>();
		ArrayList<Integer> dCounttLinkArrayList = new ArrayList<Integer>();
		ArrayList<Double> dNewValueOfVector = new ArrayList<Double>();
		
		//init pagerank array
		for( int iArrayIndex = 0; iArrayIndex < MATRIX_SIZE; iArrayIndex++ )
		{
			adPageRankArray[iArrayIndex] = 1.0 / (double) MATRIX_SIZE;
		}
		
		for( int iIterationCounter = 0; iIterationCounter < ITERATIONS; iIterationCounter++ )
		{
			//count iteratively pageRank in next steps
			for( int iIndexRowMatrix = 0; iIndexRowMatrix < MATRIX_SIZE; iIndexRowMatrix++ )
			{
				for( int iIndexColumnMatrix = 0; iIndexColumnMatrix < MATRIX_SIZE; iIndexColumnMatrix++ )
				{
					if( M[iIndexRowMatrix][iIndexColumnMatrix] > 0.0 )
					{
						dRememberIndexAnotherLinkArrayList.add( iIndexColumnMatrix );
					}
				}
				
				for( int iIndexRememberIndexAnotherLink = 0; iIndexRememberIndexAnotherLink < dRememberIndexAnotherLinkArrayList.size(); iIndexRememberIndexAnotherLink++)
				{
					int iRememberIndex = dRememberIndexAnotherLinkArrayList.get(iIndexRememberIndexAnotherLink);
					for( int i=0 ; i < MATRIX_SIZE; i++)
					{
						if( M[i][iRememberIndex] > 0 )
						{
							iLinkFromAnotherNodeCount++;
						}
					}
					
					dCounttLinkArrayList.add(iLinkFromAnotherNodeCount);
					iLinkFromAnotherNodeCount = 0;
				}
				
				for( int i=0; i < dRememberIndexAnotherLinkArrayList.size(); i++)
				{
					int iRememberIndexPageRank = dRememberIndexAnotherLinkArrayList.get(i);
					dSumOfPageRank += (double)adPageRankArray[iRememberIndexPageRank] / (double) dCounttLinkArrayList.get(i);
				}
				
				dPageRankPreviousValue = q + (1.0 - q) * dSumOfPageRank;
				
				dNewValueOfVector.add(dPageRankPreviousValue);
				
				dSumOfPageRank = 0.0;
				dCounttLinkArrayList.clear();
				dRememberIndexAnotherLinkArrayList.clear();
			}
			
			//sum for compute normalize value
			double dsumOfPageRankArray = 0.0;
			
			for( int i = 0; i < dNewValueOfVector.size(); i++)
			{
				dsumOfPageRankArray += dNewValueOfVector.get(i);
			}
			
			//new normalized value
			for( int i = 0; i < dNewValueOfVector.size(); i++)
			{
				adPageRankArray[i] = dNewValueOfVector.get(i) / dsumOfPageRankArray;
			}
			
			dNewValueOfVector.clear();
		}
		
        return adPageRankArray;
    }

    private double[] trustRank( double q ) {
        //TODO 3: compute trustrank with damping factor q (method parameter, by default q value from class constant)
        //Documents that are good = 1, 2 (indexes = 0, 1)
        //return array of TrustRank values (indexes: page number - 1, e.g. result[0] = TrustRank of page 1).
    	
    	double[] adTrustRankValues = new double[MATRIX_SIZE];
    	double[] adVectorD = GetInitializedVectorD();
    	double dSumOfValue = 0.0;
    	int iRowMatrixIndex = 0;
    	
    	adVectorD = GetNormalizedVectorD( adVectorD );
    	adTrustRankValues = GetInitTrustRankVector( adVectorD );
    	
    	for( int iIndexTrustRankVector = 0; iIndexTrustRankVector < adTrustRankValues.length; iIndexTrustRankVector++ )
    	{
    		dSumOfValue = 0.0;
    		
    		for( int iColumnMatrix = 0; iColumnMatrix < MATRIX_SIZE; iColumnMatrix++ )
    		{
    			iRowMatrixIndex = iIndexTrustRankVector;
    			dSumOfValue += M[iRowMatrixIndex][iColumnMatrix] * adVectorD[iColumnMatrix];
    		}
    		
    		adTrustRankValues[iIndexTrustRankVector] = q * adVectorD[iIndexTrustRankVector] + ( 1.0 - q ) * dSumOfValue;	
    	}
    	
        return adTrustRankValues;
    }

    private double[] GetInitTrustRankVector( double[] adVectorD ){
    	
    	double[] adTrustRankValues = new double[adVectorD.length];
    	
    	for( int iIndexTrustRankArray=0; iIndexTrustRankArray < adVectorD.length; iIndexTrustRankArray++ )
    	{
    		adTrustRankValues[iIndexTrustRankArray] = adVectorD[iIndexTrustRankArray];
    	}
    	
    	return adTrustRankValues;
	}


	private double[] GetNormalizedVectorD( double[] adVectorD ){
    	
    	double[] adNormalizedVectorD = new double[adVectorD.length];
    	double dSumOfTrustPagesVectorD = 0.0;
    	
    	for( int iIndexOfVectorD = 0; iIndexOfVectorD < adVectorD.length; iIndexOfVectorD++ )
    	{
    		if( adVectorD[iIndexOfVectorD] == 1.0)
    		{
    			dSumOfTrustPagesVectorD += 1.0;
    		}
    		
    		adNormalizedVectorD[iIndexOfVectorD] = 0.0;
    	}
    	
    	for( int iIndexOfNormalizedVectorD = 0; iIndexOfNormalizedVectorD < adVectorD.length; iIndexOfNormalizedVectorD++ )
    	{
    		if( adVectorD[iIndexOfNormalizedVectorD] == 1.0)
    		{
    			adNormalizedVectorD[iIndexOfNormalizedVectorD] = 1 / dSumOfTrustPagesVectorD;
    		}
    	}
    	
    	return adNormalizedVectorD;
	}

	private double[] GetInitializedVectorD(){
    	
    	double dValueOfTrustRank = 0.0;
    	double[] adVectorD = new double[MATRIX_SIZE];
    	
    	for( int iIndexTrustRankVectorIndex = 0; iIndexTrustRankVectorIndex < MATRIX_SIZE; iIndexTrustRankVectorIndex++ )
    	{
    		dValueOfTrustRank = 0.0;
    		
    		if( iIndexTrustRankVectorIndex == 0 || iIndexTrustRankVectorIndex == 1)
    		{
    			dValueOfTrustRank = 1.0;
    		}
    		
    		adVectorD[iIndexTrustRankVectorIndex] = dValueOfTrustRank;
    	}
    	
    	return adVectorD;
	}

	private void sortAndPrint(double[] vector) {
        Map<String, Double> map = new HashMap<>();
        for (int i = 0; i < vector.length; i++) {
            map.put((i + 1) + "", vector[i]);
        }
        map.entrySet().stream().sorted(Comparator.comparing(item -> -item.getValue()))
                .forEach(item -> System.out.println(item.getKey() + ": " + item.getValue()));
    }

    private void printMatrix(double[][] L) {
        for (double[] row : L) {
            for (double val : row)
                System.out.print(val + " ");
            System.out.println();
        }
    }
}
