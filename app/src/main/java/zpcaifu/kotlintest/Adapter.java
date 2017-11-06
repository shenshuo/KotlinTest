package zpcaifu.kotlintest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

public class Adapter extends FragmentStatePagerAdapter {
	
     private List<Fragment> mFragmentList;
     private String[] titles = {"首页","项目","我的"};
     
     public Adapter(FragmentManager fm, List<Fragment> fragList ) {
           super(fm );
           mFragmentList=fragList ;
     }
    public void UpdateList(List<Fragment> arrayList) {
        this.mFragmentList.clear();
        this.mFragmentList.addAll(arrayList);

        notifyDataSetChanged();
    }
    @Override
     public Fragment getItem(int arg0 ) {
           return mFragmentList .get(arg0 );
     }
     @Override
     public int getCount() {
           return mFragmentList .size();
     }
     
     @Override
    public CharSequence getPageTitle(int position) {
    	return titles[position];
    }
    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE;
    }
}

