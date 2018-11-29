import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PageRank {
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

    private double[][] getM(double[][] L) {
        //TODO 1: Compute stochastic matrix M
        double[][] M = new double[10][10];
        int[] c = new int[10]; //number of outgoing links
        
        int iVectorOutgoingLinksLength = c.length;
        
        for( int iVectorIndex = 0; iVectorIndex < iVectorOutgoingLinksLength; iVectorIndex++)
        {
        	c[iVectorIndex] = 1 / iVectorOutgoingLinksLength;
        }
        
        for( int iRowIndex = 0; iRowIndex < L.length; iRowIndex++ )
        {
        	for( int iColumnIndex = 0; iColumnIndex < L.length; iColumnIndex++ )
        	{
        		if( L[iColumnIndex][iRowIndex] == 0.0 )
        		{
        			M[iRowIndex][iColumnIndex] = 0.0;
        		}
        		else
        		{        			
        			for(int iTempCounter = 0; iTempCounter < L.length; iTempCounter++)
        			{
        				if( L[iTempCounter][iColumnIndex] == 1.0)
        				{
        					M[iRowIndex][iColumnIndex]++;	
        				}
        			}
        			
        			if( M[iRowIndex][iColumnIndex] > 1.0)
        			{
        				M[iRowIndex][iColumnIndex] = 1.0 / M[iRowIndex][iColumnIndex];
        			}
        		}
        	}
        }

        return M;
    }


    private double[] pageRank(double q) {
        //TODO 2: compute PageRank with damping factor q (method parameter, by default q value from class constant)
        //return array of PageRank values (indexes: page number - 1, e.g. result[0] = TrustRank of page 1).

        return new double[0];
    }

    private double[] trustRank(double q) {
        //TODO 3: compute trustrank with damping factor q (method parameter, by default q value from class constant)
        //Documents that are good = 1, 2 (indexes = 0, 1)
        //return array of TrustRank values (indexes: page number - 1, e.g. result[0] = TrustRank of page 1).

        return new double[0];
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
