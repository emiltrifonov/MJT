import java.util.Arrays;

public class TaskDistributor {
    private static boolean canFindSum(int sum, int[] arr) {
        // arr is always sorted
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] <= sum) {
                sum -= arr[i];
            }
        }

        return sum == 0;
    }
    public static int minDifference(int[] tasks) {
        if (tasks.length == 0) return 0;
        if (tasks.length == 1) return tasks[0];

        Arrays.sort(tasks);
        int sumOfTasks = Arrays.stream(tasks).sum();
        int smallSum = sumOfTasks / 2;
        int bigSum = sumOfTasks - smallSum;

        while (smallSum > 0) {
            if (canFindSum(smallSum, tasks)) {
                return bigSum - smallSum;
            }
            else {
                bigSum++;
                smallSum--;
            }
        }

        return sumOfTasks;
    }
}
