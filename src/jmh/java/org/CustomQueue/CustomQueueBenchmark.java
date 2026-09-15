package org.CustomQueue;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class CustomQueueBenchmark {

    @Param({"10000", "20000", "30000", "40000", "50000", "60000", "70000", "80000", "90000", "100000"})
    public int size;

    private List<Integer> sourceCollection;
    private CustomQueue<Integer> queue;
    private List<Integer> subCollection;
    private List<Integer> removeCollection;

    @Setup(Level.Trial)
    public void setupTrial() {
        sourceCollection = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            sourceCollection.add(i);
        }

        subCollection = new ArrayList<>(Math.max(1, size / 10));
        for (int i = 0; i < Math.max(1, size / 10); i++) {
            subCollection.add(i);
        }

        removeCollection = new ArrayList<>(size / 5);
        for (int i = 0; i < size / 5; i++) {
            removeCollection.add(i);
        }
    }

    @Setup(Level.Invocation)
    public void setupInvocation() {
        queue = new CustomQueue<>(sourceCollection);
    }

    @Benchmark
    public CustomQueue<Integer> testConstructorDefault() {
        return new CustomQueue<>();
    }

    @Benchmark
    public CustomQueue<Integer> testConstructorCapacity() {
        return new CustomQueue<>(size);
    }

    @Benchmark
    public CustomQueue<Integer> testConstructorCollection() {
        return new CustomQueue<>(sourceCollection);
    }

    @Benchmark
    public boolean testAdd() {
        CustomQueue<Integer> q = new CustomQueue<>();
        for (int i = 0; i < size; i++) {
            q.add(i);
        }
        return true;
    }

    @Benchmark
    public boolean testOffer() {
        CustomQueue<Integer> q = new CustomQueue<>();
        for (int i = 0; i < size; i++) {
            q.offer(i);
        }
        return true;
    }

    @Benchmark
    public Integer testElement() {
        return queue.element();
    }

    @Benchmark
    public Integer testPeek() {
        return queue.peek();
    }

    @Benchmark
    public Integer testPoll() {
        CustomQueue<Integer> q = new CustomQueue<>(sourceCollection);
        return q.poll();
    }

    @Benchmark
    public Integer testRemoveHead() {
        CustomQueue<Integer> q = new CustomQueue<>(sourceCollection);
        return q.remove();
    }

    @Benchmark
    public boolean testRemoveObject() {
        CustomQueue<Integer> q = new CustomQueue<>(sourceCollection);
        return q.remove(Integer.valueOf(size / 2));
    }

    @Benchmark
    public int testSize() {
        return queue.size();
    }

    @Benchmark
    public boolean testIsEmpty() {
        return queue.isEmpty();
    }

    @Benchmark
    public void testClear() {
        CustomQueue<Integer> q = new CustomQueue<>(sourceCollection);
        q.clear();
    }

    @Benchmark
    public boolean testContains() {
        return queue.contains(size / 2);
    }

    @Benchmark
    public boolean testContainsAll() {
        return queue.containsAll(subCollection);
    }

    @Benchmark
    public boolean testAddAll() {
        CustomQueue<Integer> q = new CustomQueue<>();
        return q.addAll(sourceCollection);
    }

    @Benchmark
    public boolean testRemoveAll() {
        CustomQueue<Integer> q = new CustomQueue<>(sourceCollection);
        return q.removeAll(removeCollection);
    }

    @Benchmark
    public boolean testRetainAll() {
        CustomQueue<Integer> q = new CustomQueue<>(sourceCollection);
        return q.retainAll(removeCollection);
    }

    @Benchmark
    public Object[] testToArray() {
        return queue.toArray();
    }

    @Benchmark
    public Integer[] testToArrayWithType() {
        return queue.toArray(new Integer[0]);
    }

    @Benchmark
    public String testToString() {
        return queue.toString();
    }

    @Benchmark
    public int testIterator() {
        int sum = 0;
        Iterator<Integer> it = queue.iterator();
        while (it.hasNext()) {
            sum += it.next();
        }
        return sum;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CustomQueueBenchmark.class.getSimpleName())
                .forks(1)
                .result("custom-queue-results.csv")
                .resultFormat(ResultFormatType.CSV)
                .build();

        Collection<RunResult> results = new Runner(opt).run();
        writeCustomCsv(results);
    }

    private static void writeCustomCsv(Collection<RunResult> results) {
        try (FileWriter writer = new FileWriter("CustomQueue_jmh_performance.csv")) {
            writer.write("Benchmark;Size;Score (ns/op)\n");
            for (RunResult result : results) {
                String benchmarkName = result.getParams().getBenchmark();
                String shortName = benchmarkName.substring(benchmarkName.lastIndexOf('.') + 1);

                double score = result.getPrimaryResult().getScore();
                String sizeVal = result.getParams().getParam("size");

                writer.write("\"" + shortName + "\";" + (sizeVal != null ? sizeVal : "N/A") + ";" + score + "\n");
            }
            System.out.println("JMH Performance report saved: CustomQueue_jmh_performance.csv");
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }
}