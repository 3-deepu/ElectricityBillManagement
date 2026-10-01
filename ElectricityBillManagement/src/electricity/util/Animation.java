package electricity.util;

public final class Animation {
    private Animation() {}

    public static void loading(String message, int cycles) {
        System.out.print(Colors.PURPLE + message + " " + Colors.RESET);
        String[] frames = {"[>        ]", "[=>       ]", "[==>      ]", "[===>     ]",
                           "[====>    ]", "[=====>   ]", "[======>  ]", "[=======> ]", "[========]"};
        for (int c = 0; c < cycles; c++) {
            for (String frame : frames) {
                System.out.print("\r" + Colors.PURPLE + message + " " + frame + Colors.RESET);
                sleep(80);
            }
        }
        System.out.println("\r" + Colors.GREEN + message + " [########] DONE" + Colors.RESET);
    }

    public static void dots(String message, int count) {
        System.out.print(Colors.YELLOW + message + Colors.RESET);
        for (int i = 0; i < count; i++) {
            System.out.print(".");
            sleep(250);
        }
        System.out.println();
    }

    public static void typeLine(String text, long delay) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            sleep(delay);
        }
        System.out.println();
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
