package io.rou.event.sequence;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.rou.event.sequence.EventSequence.Task;
import io.rou.event.sequence.EventSequence.TaskObserver;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class EventSequenceTest {
	private int mCalledIndex;

	@Before
	public void setUp() {
		mCalledIndex = 0;
	}

	@Test
	public void 正しい順序で複数のタスクが実行される() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(4);

		Task[] tasks = {
				(TaskObserver observer) -> {
					assertThat(mCalledIndex++, is(0));
					latch.countDown();
					observer.onFinish();
				},
				(TaskObserver observer) -> {
					assertThat(mCalledIndex++, is(1));
					latch.countDown();
					observer.onFinish();
				},
				(TaskObserver observer) -> {
					assertThat(mCalledIndex++, is(2));
					latch.countDown();
					observer.onFinish();
				}
		};
		EventSequence sequence = new EventSequence(tasks, latch::countDown);
		sequence.start();

		latch.await(1, TimeUnit.SECONDS);
	}
}
