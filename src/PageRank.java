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
        System.out.println("Trust rank before changes in Matrix L");
        sortAndPrint(trustRank(q));
        L[0][4] = 0;
        L[2][6] = 0;
        M = getM(L);
        System.out.println("New Matrix M after removing connections: 1->5 and 3->7");
        printMatrix(M);
        System.out.println("Trust rank afer changes in Matrix L");
        sortAndPrint(trustRank(q));
    }

    private double[][] getM( double[][] L ) {
        //TODO 1: Compute stochastic matrix M
    	//DONE!
    	
        int[] c = GetNumbersOfOutgoingLinksMatrix( L );		// vector with number of outgoing links
        double[][] M = GetStochasticMatrix( L, c );  		// stochastic matrix

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
		
		double[] adPageRankArray = GetInitializedPageRankArray();
		double dPageRankNewValue = 0.0;
		double dSumOfPageRankPart = 0.0;
		ArrayList<Integer> dRememberIndexAnotherLinkArrayList = new ArrayList<Integer>();
		ArrayList<Integer> dCountLinkArrayList = new ArrayList<Integer>();
		ArrayList<Double> dNewValueOfVector = new ArrayList<Double>();
		
		for( int iIterationCounter = 0; iIterationCounter < ITERATIONS; iIterationCounter++ )
		{
			//count iteratively pageRank in next steps
			for( int iIndexRowMatrix = 0; iIndexRowMatrix < MATRIX_SIZE; iIndexRowMatrix++ )
			{
				
				dRememberIndexAnotherLinkArrayList = GetRememberArrayLinkList( iIndexRowMatrix );
				dCountLinkArrayList = GetLinkCountFromAnotherNodesArrayList( dRememberIndexAnotherLinkArrayList );
				dSumOfPageRankPart = GetSumOfPageRank( dRememberIndexAnotherLinkArrayList, adPageRankArray, dCountLinkArrayList );
				
				dPageRankNewValue = q + ( 1.0 - q ) * dSumOfPageRankPart;
				dNewValueOfVector.add( dPageRankNewValue );
			}
			
			adPageRankArray = GetNormalizedPageRankVector( adPageRankArray, dNewValueOfVector );
			dNewValueOfVector.clear();
		}
		
        return adPageRankArray;
    }

    private double[] GetNormalizedPageRankVector( double[] adPageRankArray, ArrayList<Double> dNewValueOfVector ) {

    	double dsumOfPageRankArray = 0.0;
		
		for( int iCounter = 0; iCounter < dNewValueOfVector.size(); iCounter++ )
		{
			dsumOfPageRankArray += dNewValueOfVector.get( iCounter );
		}
		
		for( int iCounter = 0; iCounter < dNewValueOfVector.size(); iCounter++ )
		{
			adPageRankArray[iCounter] = dNewValueOfVector.get( iCounter ) / dsumOfPageRankArray;
		}
		
		return adPageRankArray;
	}

	private double GetSumOfPageRank( ArrayList<Integer> dRememberIndexAnotherLinkArrayList, 
    		double[] adPageRankArray, 
    		ArrayList<Integer> dCountLinkArrayList ){
		
    	double dSumOfPageRankPart = 0.0;
    	
    	for( int iCounter=0; iCounter < dRememberIndexAnotherLinkArrayList.size(); iCounter++)
		{
			int iRememberIndexPageRank = dRememberIndexAnotherLinkArrayList.get( iCounter );
			dSumOfPageRankPart += (double)adPageRankArray[iRememberIndexPageRank] / (double) dCountLinkArrayList.get( iCounter );
		}
    	
    	return dSumOfPageRankPart;
	}

	private ArrayList<Integer> GetLinkCountFromAnotherNodesArrayList(
			ArrayList<Integer> dRememberIndexAnotherLinkArrayList ){
    	
		int iLinkFromAnotherNodeCount = 0;
		ArrayList<Integer> dCountLinkArrayList = new ArrayList<Integer>();
		
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
			
			dCountLinkArrayList.add( iLinkFromAnotherNodeCount );
			iLinkFromAnotherNodeCount = 0;
		}
    	
    	return dCountLinkArrayList;
	}

	private ArrayList<Integer> GetRememberArrayLinkList( int iIndexRowMatrix ){
		
    	ArrayList<Integer> dRememberIndexAnotherLinkArrayList = new ArrayList<Integer>();
    	
    	for( int iIndexColumnMatrix = 0; iIndexColumnMatrix < MATRIX_SIZE; iIndexColumnMatrix++ )
		{
			if( M[iIndexRowMatrix][iIndexColumnMatrix] > 0.0 )
			{
				dRememberIndexAnotherLinkArrayList.add( iIndexColumnMatrix );
			}
		}
    	
    	return dRememberIndexAnotherLinkArrayList;
	}


	private double[] GetInitializedPageRankArray() {
		double[] adPageRankArray = new double[MATRIX_SIZE];
		
		for( int iArrayIndex = 0; iArrayIndex < MATRIX_SIZE; iArrayIndex++ )
		{
			adPageRankArray[iArrayIndex] = 1.0 / (double) MATRIX_SIZE;
		}
		
		return adPageRankArray;
	}


	private double[] trustRank( double q ) {
        //TODO 3: compute trustrank with damping factor q (method parameter, by default q value from class constant)
        //Documents that are good = 1, 2 (indexes = 0, 1)
        //return array of TrustRank values (indexes: page number - 1, e.g. result[0] = TrustRank of page 1).
    	//DONE!
    	
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
