package mlcs.np;

import java.util.*;

public class MyBestOrderSort {

    static int m1 = -1;
    static int population[][];
    static int[][] allrank;
    static MergeSort mergesort;
    static int rank[];
    static LinkedList[][] list;
    static LinkedList[] L;// two dimension
    static double b[];// two dimension
    static int totalfront = 0;
    static int s;
    static int n;
    static int m;
    static int[] lex_order;
    static int filterRank[];
    static int[] sum;
    static double comparison, comparison_sort, comparison_rank;

    public static void best_sort(int n, int m) {
        int i, j, i2;
        boolean dominated;
        Node head;

        rank = new int[n];
        boolean[] set = new boolean[n];
        list = new LinkedList[m][n];
        allrank = new int[n][m];
        lex_order = new int[n];
        filterRank = new int[n];

        for (j = 0; j < m; j++) {
            for (i = 0; i < n; i++) {
                list[j][i] = new LinkedList();
                allrank[i][j] = i;
            }
        }

        for (i = 0; i < n; i++) {
            filterRank[i] = i;
            rank[i] = -1;
        }

        mergesort.setrank(allrank);
        sortingValues();


        MergeSortSum sortSum = new MergeSortSum();
        sortSum.setPopulationAndSum(population, sum);
        sortSum.setFilterRank(filterRank);
        sortSum.sort();
        filterRank = sortSum.getFilterRank();

        int filter = filterRank[0];
        int filterIndex = 0;
        rank[filter] = 0;
        List<Integer> objList = new ArrayList<>();
        for (int x = 0; x < m; x++) {
            objList.add(x);
        }
        for (i = 0; i < n; i++)//n
        {

            Iterator<Integer> objIter = objList.iterator();
            while (objIter.hasNext()) {
                int obj = objIter.next();
                s = allrank[i][obj];
                if (s == filter) {
                    list[obj][rank[s]].addStart(s);
                    objIter.remove();
                    continue;
                }

                if (set[s])//s is already ranked
                {
                    list[obj][rank[s]].addStart(s);
                    continue;
                }
                set[s] = true;
                i2 = 0;

                while (true) {
                    dominated = false;
                    head = list[obj][i2].start;
                    while (head != null) {
                        if (dominates(head.data, s)) {
                            dominated = true;
                            break;
                        }
                        head = head.link;

                    }

                    if (!dominated) //not dominated
                    {
                        list[obj][i2].addStart(s);
                        rank[s] = i2;
                        break;
                    } else //dominated
                    {
                        if (i2 < totalfront) {
                            i2++;
                        } else //if it is last node(with highest rank)
                        {

                            totalfront++;
                            rank[s] = totalfront;
                            list[obj][rank[s]].addStart(s);
                            break;
                        }
                    }

                }//while(true)
            }
            if (objList.size() == 0)
                break;
        }
        totalfront++;
    }

    /**
     * Non-domination check, Although previously sorted by lexicographic order it
     * may be true that those solutions are identical
     *
     * @param p1
     * @param p2
     * @return
     */
    public static boolean dominates(int p1, int p2)// one way domination check
    {
        boolean equal = true;
        int i;
        for (i = 0; i < m; i++) {

            if (population[p1][i] > population[p2][i]) {
                comparison_rank++;
                return false;
            } else if (equal && population[p1][i] < population[p2][i]) {
                comparison_rank = comparison_rank + 2;
                equal = false;
            }
        }

        if (equal)// both solutions are equal
            return false;
        else // dominates
            return true;
    }

    /**
     * Sorting population by each objective
     */
    public static void sortingValues() {
        int j;
        mergesort.sort(0);// Sorting first objectives and get lexicographic order
        comparison_sort = comparison_sort + mergesort.comparison;
        allrank = mergesort.getrank();
        for (j = 1; j < n; j++) {
            lex_order[allrank[j][0]] = j;
        }
        mergesort.setLexOrder(lex_order);
        for (j = 1; j < m1; j++) {
            mergesort.sort_specific(j);
            comparison_sort = comparison_sort + mergesort.comparison;
        }
        allrank = mergesort.getrank();
    }

    /**
     * Initialize all variables
     *
     * @param population2
     */
    private static void initialize(int[][] population2, int[] sum2) {
        population = population2;
        sum = sum2;
        mergesort = new MergeSort();
        n = population.length;
        m = population[0].length;
        mergesort.setPopulation(population);
        comparison = 0;
        m1 = m;// change this value to m1 = log(n) when m is very high
    }

    /**
     * This algorithm runs O(nlogn) algorithm for two dimensional non-dominated
     * sorting problem
     */
    public static void extended_kung_sort_two_dimension() {
        // Initialization
        int i, j, low, high, middle;
        double key;

        rank = new int[n];// ranks of solutions
        allrank = new int[n][m];// partial lexicographical ordering of x-axis values
        // b = new double[n];
        int[] index = new int[n];
        L = new LinkedList[n];
        for (j = 0; j < m; j++) {
            for (i = 0; i < n; i++) {
                allrank[i][j] = i;
            }
        }
        mergesort.setrank(allrank);
        mergesort.sort(0);
        comparison_sort = mergesort.comparison;


        // b[0] = population[allrank[0][0]][1];//y-value of first rank solution
        index[0] = allrank[0][0];
        rank[allrank[0][0]] = 0; // rank of first solution is already found
        totalfront = 1;

        for (i = 1; i < n; i++) {
            s = allrank[i][0];// take the solution id
            key = population[s][1];// the field we would consider

            // -------------Go over all points----------------------//
            low = 0;
            high = totalfront - 1;

            while (high >= low) {
                middle = (low + high) / 2;

                if (key < population[index[middle]][1]) // it has low rank, numerically
                {
                    comparison_sort++;
                    high = middle - 1;
                } else if (key > population[index[middle]][1]) // it has high rank, numerically
                {
                    comparison_sort = comparison_sort + 2;
                    low = middle + 1;
                } else {
                    comparison_sort = comparison_sort + 2;
                    if (population[index[middle]][0] < population[s][0]) {
                        low = middle + 1;
                    } else// first objective was also same
                    {
                        low = rank[index[middle]];
                        break;
                    }
                }
            }

            if (low == totalfront) {
                totalfront = totalfront + 1;
            }
            rank[s] = low;
            index[low] = s;
        }
    }

    /**
     * Class that takes care of lexicographic sorting of objectives
     *
     * @author Proteek Roy
     */
    static class MergeSort {
        // local variables
        int[] helper;
        int[][] population;
        int n;
        int obj;
        boolean check;
        int[][] rank;
        int comparison;
        int[] lex_order;

        public void setrank(int[][] rank)// set ranking information
        {
            this.rank = rank;
        }

        public int[][] getrank()// get ranking information
        {
            return rank;
        }

        public void setPopulation(int[][] pop)// set local population
        {
            this.population = pop;
            this.n = population.length;
            helper = new int[n];
        }

        public void setLexOrder(int[] order) {
            this.lex_order = order;
        }

        /**
         * Sort objectives obj
         *
         * @param obj
         */

        public void sort(int obj)// merge sort main
        {
            this.obj = obj;
            comparison = 0;
            n = population.length;
            mergesort(0, n - 1);
        }

        /**
         * merge sort algorithm O(nlogn) sorting time
         */
        private void mergesort(int low, int high) {
            // check if low is smaller then high, if not then the array is sorted
            if (low < high) {
                // Get the index of the element which is in the middle
                int middle = low + (high - low) / 2;
                // Sort the left side of the array
                mergesort(low, middle);
                // Sort the right side of the array
                mergesort(middle + 1, high);
                // Combine them both
                merge(low, middle, high);
            }
        }

        private void merge(int low, int middle, int high) {
            // Copy both parts into the helper array
            for (int i = low; i <= high; i++) {
                helper[i] = rank[i][obj];
            }

            int i = low;
            int j = middle + 1;
            int k = low;
            // Copy the smallest values from either the left or the right side back
            // to the original array
            while (i <= middle && j <= high) {
                if (population[helper[i]][obj] < population[helper[j]][obj]) {
                    comparison++;
                    rank[k][obj] = helper[i];
                    i++;
                } else if (population[helper[i]][obj] > population[helper[j]][obj]) {
                    comparison = comparison + 2;
                    rank[k][obj] = helper[j];
                    j++;
                } else // two values are equal
                {
                    comparison = comparison + 2;
                    check = lexicopgraphic_dominate(helper[i], helper[j]);
                    if (check) {
                        rank[k][obj] = helper[i];
                        i++;
                    } else {
                        rank[k][obj] = helper[j];
                        j++;
                    }
                }
                k++;
            }
            while (i <= middle) {
                rank[k][obj] = helper[i];
                k++;
                i++;
            }
            while (j <= high) {
                rank[k][obj] = helper[j];
                k++;
                j++;
            }

        }

        public void sort_specific(int obj)// uses lexicographical order of 1st dimension
        {
            this.obj = obj;
            n = population.length;
            comparison = 0;
            mergesort_specific(0, n - 1);
        }

        /**
         * Merge sort with the help of lexicographic order got by sorting first
         * objectives
         *
         * @param low
         * @param high
         */
        private void mergesort_specific(int low, int high) {
            // check if low is smaller then high, if not then the array is sorted
            if (low < high) {
                // Get the index of the element which is in the middle
                int middle = low + (high - low) / 2;
                // Sort the left side of the array
                mergesort_specific(low, middle);
                // Sort the right side of the array
                mergesort_specific(middle + 1, high);
                // Combine them both
                merge_specific(low, middle, high);
            }

        }

        private void merge_specific(int low, int middle, int high) {

            // Copy both parts into the helper array
            for (int i = low; i <= high; i++) {
                helper[i] = rank[i][obj];
            }

            int i = low;
            int j = middle + 1;
            int k = low;
            // Copy the smallest values from either the left or the right side back to the
            // original array
            while (i <= middle && j <= high) {
                if (population[helper[i]][obj] < population[helper[j]][obj]) {
                    comparison++;
                    rank[k][obj] = helper[i];
                    i++;
                } else if (population[helper[i]][obj] > population[helper[j]][obj]) {
                    comparison = comparison + 2;
                    rank[k][obj] = helper[j];
                    j++;
                } else // two values are equal
                {
                    comparison = comparison + 2;
                    if (lex_order[helper[i]] < lex_order[helper[j]]) {
                        rank[k][obj] = helper[i];
                        i++;
                    } else {
                        rank[k][obj] = helper[j];
                        j++;
                    }
                }
                k++;
            }
            while (i <= middle) {
                rank[k][obj] = helper[i];
                k++;
                i++;
            }
            while (j <= high) {
                rank[k][obj] = helper[j];
                k++;
                j++;
            }

        }

        /**
         * check whether p1 lexicographically dominates p2
         *
         * @param p1
         * @param p2
         * @return
         */
        public boolean lexicopgraphic_dominate(int p1, int p2) {
            int i;

            for (i = 0; i < population[0].length; i++) {
                comparison++;
                if (population[p1][i] == population[p2][i])
                    continue;
                else if (population[p1][i] < population[p2][i]) {
                    return true;
                } else {
                    return false;
                }
            }

            return true;
        }

    }

    static class MergeSortSum {
        //local variables
        int[] helper;
        int[] sum;
        int[][] population;
        int n;
        boolean check;
        int comparison;
        int[] filterRank;

        public void setFilterRank(int[] filterRank) {
            this.filterRank = filterRank;
        }

        public int[] getFilterRank() {
            return this.filterRank;
        }

        public void setPopulationAndSum(int[][] population, int[] sum)//set local population
        {
            this.sum = sum;
            this.population = population;
            this.n = sum.length;
            helper = new int[n];
        }

        public void sort()//merge sort main
        {
            comparison = 0;
            n = population.length;
            mergesort(0, n - 1);
        }

        /**
         * merge sort algorithm O(nlogn) sorting time
         */
        private void mergesort(int low, int high) {
            // check if low is smaller then high, if not then the array is sorted
            if (low < high) {
                // Get the index of the element which is in the middle
                int middle = low + (high - low) / 2;
                // Sort the left side of the array
                mergesort(low, middle);
                // Sort the right side of the array
                mergesort(middle + 1, high);
                // Combine them both
                merge(low, middle, high);
            }
        }

        private void merge(int low, int middle, int high) {
            // Copy both parts into the helper array
            for (int i = low; i <= high; i++) {
                helper[i] = filterRank[i];
            }

            int i = low;
            int j = middle + 1;
            int k = low;
            // Copy the smallest values from either the left or the right side back
            // to the original array
            while (i <= middle && j <= high) {
                if (sum[helper[i]] < sum[helper[j]]) {
                    comparison++;
                    filterRank[k] = helper[i];
                    i++;
                } else if (sum[helper[i]] > sum[helper[j]]) {
                    comparison = comparison + 2;
                    filterRank[k] = helper[j];
                    j++;
                } else //two values are equal
                {
                    comparison = comparison + 2;
                    check = lexicopgraphic_dominate(helper[i], helper[j]);
                    if (check) {
                        filterRank[k] = helper[i];
                        i++;
                    } else {
                        filterRank[k] = helper[j];
                        j++;
                    }
                }
                k++;
            }
            while (i <= middle) {
                filterRank[k] = helper[i];
                k++;
                i++;
            }
            while (j <= high) {
                filterRank[k] = helper[j];
                k++;
                j++;
            }

        }


        public boolean lexicopgraphic_dominate(int p1, int p2) {
            int i;

            for (i = 0; i < population[0].length; i++) {
                comparison++;
                if (population[p1][i] == population[p2][i])
                    continue;
                else if (population[p1][i] < population[p2][i]) {
                    return true;
                } else {
                    return false;
                }
            }

            return true;
        }

    }

    /**
     * Class to save solutions in a list
     *
     * @author Proteek Roy
     */
    public static class LinkedList {
        protected Node start;

        public LinkedList()// Constructor
        {
            start = null;
        }

        // Function to insert an element at the beginning
        public void addStart(int val) {
            Node nptr = new Node(val, start);
            start = nptr;
        }
    }

    public static class Node {
        protected int data;
        protected Node link;

        // Constructor
        public Node(int d, Node n) {
            data = d;
            link = n;
        }
    }


    public static Set<Location> sortAndSelect(ArrayList<Location> nondominatedList, int seletedNum) {
        Set<Location> result = new HashSet<Location>();
        int n = nondominatedList.size();
        int m = nondominatedList.get(0).index.length;
        int[][] population = new int[n][m];
        int[] sum = new int[n];
        int i = 0;
        for (Location loc : nondominatedList) {
            population[i] = loc.index;
            sum[i] = loc.indexSum;
            i++;
        }
        initialize(population, sum);

        if (m == 2) {
            extended_kung_sort_two_dimension();
        } else {
            best_sort(n, m);
        }

        //Select the location that meet the requirements
        Set<Integer> minDimensionIndexSet = new HashSet<>();
        for (i = 0; i < m; i++) {
            minDimensionIndexSet.add(allrank[0][i]);
        }

        List<Integer> sortRestult = new ArrayList<>();
        for (i = 0; i < n; i++) {
            int index = filterRank[i];
            if (rank[index] == 0)
                sortRestult.add(index);
        }
        double[] score = new double[sortRestult.size()];
        boolean[] seleted = new boolean[sortRestult.size()];
        for (i = 0; i < sortRestult.size(); i++) {
            seleted[i] = true;
            Location loc = nondominatedList.get(sortRestult.get(i));
            score[i] = loc.indexSum + loc.maxIndex;
        }

        int num = Math.min(seletedNum, sortRestult.size());

        for (int l = 0; l < num; l++) {
            int index = -1;
            double minValue = Double.MAX_VALUE;
            for (i = 0; i < sortRestult.size(); i++) {
                if (seleted[i] && score[i] < minValue) {
                    minValue = score[i];
                    index = i;
                }
            }
            seleted[index] = false;
            result.add(nondominatedList.get(sortRestult.get(index)));
        }


        return result;
    }

}
