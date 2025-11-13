/* to remove duplicate elements from a sorted array */

public class Duplicates {
    public static void main(String[] args) {
        int[] arr = {1, 2, 2, 3, 3, 4, 5, 5, 6};

        int n = arr.length;
        int[] temp = new int[n];
        int j = 0;
        temp[j++] = arr[0];
        for (int i = 1; i < n; i++) {
            if (arr[i] != arr[i - 1]) {
                temp[j] = arr[i];
                j++;
            }
        }

        System.out.print("Array after removing duplicates: ");
        for (int i = 0; i < j; i++) {
            System.out.print(temp[i] + " ");
        }
    }
}
