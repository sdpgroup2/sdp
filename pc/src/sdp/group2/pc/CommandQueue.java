package sdp.group2.pc;


import sdp.group2.util.Constants;

import java.util.concurrent.ConcurrentLinkedQueue;


public class CommandQueue {
    private static ConcurrentLinkedQueue<int[]> commandQueue2A = new ConcurrentLinkedQueue<int[]>();
    private static ConcurrentLinkedQueue<int[]> commandQueue2D = new ConcurrentLinkedQueue<int[]>();

    public static void add(int[] command, String robotName) {
        if (robotName.equals(Constants.ROBOT_2A_NAME)) {
            commandQueue2A.add(command);
        } else if (robotName.equals(Constants.ROBOT_2D_NAME)) {
            commandQueue2D.add(command);
        }
    }

    public static int[] poll(String robotName) {
        if (robotName.equals(Constants.ROBOT_2A_NAME)) {
            return commandQueue2A.poll();
        } else if (robotName.equals(Constants.ROBOT_2D_NAME)) {
            return commandQueue2D.poll();
        } else return new int[]{0, 0, 0, 0};
    }

    public static boolean isEmpty(String robotName) {
        if (robotName.equals(Constants.ROBOT_2A_NAME)) {
            return commandQueue2A.isEmpty();
        } else if (robotName.equals(Constants.ROBOT_2D_NAME)) {
            return commandQueue2D.isEmpty();
        } else {
            return true;
        }
    }

    public static void clear(String robotName) {
        if (robotName.equals(Constants.ROBOT_2A_NAME)) {
            // TODO: this was breaking the build, please fix it Gordon
            //commandQueue2A.add();
        } else if (robotName.equals(Constants.ROBOT_2D_NAME)) {
            commandQueue2D.clear();
        }
    }

    public static boolean containsCommand(short command, String robotName) {
        if (robotName.equals(Constants.ROBOT_2A_NAME)) {
            return commandQueue2A.contains(command);
        } else if (robotName.equals(Constants.ROBOT_2D_NAME)) {
            return commandQueue2D.contains(command);
        } else {
            return false;
        }
    }
}
