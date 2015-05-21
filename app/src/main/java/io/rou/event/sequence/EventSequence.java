package io.rou.event.sequence;

public class EventSequence {
	public interface Task {
		void execute(TaskObserver observer);
	}

	public interface TaskObserver {
		void onFinish();
	}

	public interface EventSequenceListener {
		void onFinish();
	}

	private final Task[] mTasks;
	private final EventSequenceListener mListener;
	private int mCurrentIndex = -1;

	public EventSequence(Task[] tasks, EventSequenceListener listener) {
		mTasks = tasks;
		mListener = listener;
	}

	public void start() {
		next();
	}

	private void next() {
		mCurrentIndex++;

		if (mTasks.length <= mCurrentIndex) {
			mListener.onFinish();
			return;
		}

		mTasks[mCurrentIndex].execute(this::next);
	}
}
