package com.example.views;

import com.example.cardmanager.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

public class NoScrollListView extends LinearLayout {

	public NoScrollListView(Context context) {
		super(context);
		this.context = context;
		initAttr(context, null);
	}

	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initAttr(context, attrs);
	}

	/**
	 * ���÷���
	 * 
	 * @param attrs
	 */
	public void initAttr(Context context, AttributeSet attrs) {
		setOrientation(VERTICAL);
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs,
					R.styleable.NoScrollListView);
			dividerColor = typedArray.getColor(
					R.styleable.NoScrollListView_dividerColor, 0x000000000);
			dividerHeight = typedArray.getDimensionPixelOffset(
					R.styleable.NoScrollListView_dividerHeight, 0);
			typedArray.recycle();
		}
	}

	private Context context;
	private Adapter adapter;
	private int dividerHeight = 0;
	private int dividerColor = 0x00000000;

	public void setAdapter(Adapter adapter) {
		this.adapter = adapter;
		if (adapter != null) {
			adapter.registerDataSetObserver(new DataSetObserver() {

				@Override
				public void onChanged() {
					// TODO Auto-generated method stub
					super.onChanged();
					notifyDataSetChanged();
				}
			});
		}
		notifyDataSetChanged();
	}

	private OnNoScrollItemClickListener mOnItemClickListener;
	private OnNoScrollItemLongClcikListener mOnItemLongClickListener;

	public void setOnItemClickListener(
			OnNoScrollItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}

	public void setOnItemLongClickListener(
			OnNoScrollItemLongClcikListener onItemLongClickListener) {
		this.mOnItemLongClickListener = onItemLongClickListener;
	}

	private void notifyDataSetChanged() {
		removeAllViews();
		for (int i = 0; i < adapter.getCount(); i++) {
			View v = adapter.getView(i, findViewWithTag(i), this);
			v.setTag(i);
			if (mOnItemClickListener != null) {
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int index = (Integer) arg0.getTag();
						mOnItemClickListener.onItemClick(arg0, index,
								adapter.getItemId(index));
					}
				});
			}
			if(mOnItemLongClickListener!=null){
				v.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View arg0) {
						// TODO Auto-generated method stub
						int index = (Integer) arg0.getTag();
						mOnItemLongClickListener.onItemClick(arg0, index,
								adapter.getItemId(index));
						return true;
					}
				});
			}
			addView(v);
			if (dividerHeight != 0 && i != adapter.getCount() - 1) {
				View view = new View(context);
				view.setLayoutParams(new LayoutParams(
						android.view.ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight));
				view.setBackgroundColor(dividerColor);
				addView(view);
			}
		}
		System.gc();
	}

	public interface OnNoScrollItemClickListener {
		public void onItemClick(View v, int position, long id);
	}

	public interface OnNoScrollItemLongClcikListener {
		public void onItemClick(View v, int position, long id);
	}
}
