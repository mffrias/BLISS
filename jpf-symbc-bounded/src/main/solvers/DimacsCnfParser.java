package solvers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


public class DimacsCnfParser
{
    public static MiniSat220 parse(String fileName, MiniSat220 solver)
    {
		long t0 = System.currentTimeMillis();

        try
        {
            Scanner in = new Scanner(new FileInputStream(fileName));
			
            //ignore comment header
            String problemLine = in.nextLine();
            while (problemLine.startsWith("c"))
            {
                problemLine = in.nextLine();
            }
            //process the problem line   
            String[] params = problemLine.split(" ");
            if (!params[0].equals("p"))
            {
                System.err.println("ERROR: Dimacs CNF file appears to miss the problem line!");
                return solver;
            }
            if (!params[1].equals("cnf"))
            {
                System.err.println("ERROR: Parsing a non-CNF Dimacs file with the Dimacs CNF parser!");
				return solver;
            }
            
            String currentLine;
            String[] tokens;
            List<Integer> currentClause = new ArrayList<Integer>();
            //read in clauses and comment lines
            int lineID = 0;
            while (in.hasNext())
            {
            	//Dimacs splites information with a 0, however nearly everybody implements it with a new line            	
                currentLine = in.nextLine();
                lineID++;
                //System.err.println("line #" + lineID);
                tokens = currentLine.split("\\s");
                if (tokens[0].equals("c"))
                {
                    //ignore comments
                }
                else
                {
                    for (int i = 0; i < tokens.length; i++)
                    {
                        //System.err.println("  token #" + i + ": " + tokens[i]);

                        Integer literal = Integer.parseInt(tokens[i]);

                        if (literal == 0)
                        {
							int n = currentClause.size();
							int[] clause = new int[n];
							int j = 0;
							for (Integer elem : currentClause) {
								if (j == n) {
									break;
								}
								clause[j++] = elem;
							}
							
                            boolean ok = solver.addClause(clause);
							assert ok;
							//System.err.println("DimacsCnfParser.parse(): Added clause: " + Arrays.toString(clause));
                            currentClause = new ArrayList<Integer>();
                            break;
                        }
                        else
                        {
							while (Math.abs(literal) > solver.numberOfVariables()) {
								//int howManyNeedToAdd = Math.abs(literal) - solver.numberOfVariables();
								//System.err.println("adding " + howManyNeedToAdd + " vars");
								//solver.addVariables(howManyNeedToAdd);
								solver.addVariables(1);
							}
                            currentClause.add(literal);
                        }
                    }      
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("ERROR: Dimacs CNF file not found: " + fileName);
        }
		
		long t1 = System.currentTimeMillis();
		double s_elapsed = (t1 - t0) / 1000.0;

		System.out.println("CNF parsing time: " + s_elapsed + " s");
		
        return solver;
    }
	
	
	
	public static void main(String[] args) {
		
		MiniSat220 solver = new MiniSat220();
		
		solver = parse("/Users/nrosner/k8.cnf", solver);
		
		System.out.println(solver.numberOfVariables() + " variables");
		System.out.println(solver.numberOfClauses() + " clauses");
		
	}

}
