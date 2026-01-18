
class RateLimiterClass {

    private final int capacity;
    private final int refillRate; // tokens per second

    private double tokens;
    private long lastRefillTime;

    public RateLimiterClass(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTime = System.nanoTime();
    }

    public synchronized boolean allowRequest() {
        refillTokens();

        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    private void refillTokens() {
        long now = System.nanoTime();

        double secondsPassed = (now - lastRefillTime) / 1_000_000_000.0;

        double tokensToAdd = secondsPassed * refillRate;

        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }
}

public class RateLimiter {

    public static void main(String[] args) {
        RateLimiterClass limiter = new RateLimiterClass(5, 5);
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                boolean allowed = limiter.allowRequest();
                System.out.println(
                        Thread.currentThread().getName()
                        + " -> " + (allowed ? "ALLOWED" : "BLOCKED")
                );

                try {
                    Thread.sleep(200); // simulate request interval
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        for (int i = 0; i < 3; i++) {
            new Thread(task, "Thread-" + i).start();
        }
    }
}
