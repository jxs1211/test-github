package xianjie.shen.weixin60;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabFragment extends Fragment
{

	private String				mTitle	= "Default";
	public static final String	TITLE	= "title";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// 判断getArguments不为空
		if (getArguments() != null)
		{
			mTitle = getArguments().getString(TITLE);
		}
		TextView tv = new TextView(getActivity());
		tv.setTextSize(20);
		tv.setTextColor(Color.parseColor("#97E153"));
		tv.setGravity(Gravity.CENTER);
		tv.setText(mTitle);
		return tv;
	}

}
