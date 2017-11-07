package zpcaifu.kotlintest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.text.DecimalFormat;
import java.util.List;

import zpcaifu.kotlintest.utils.BitmapUtils;
import zpcaifu.kotlintest.utils.Regular;

//import com.rmbbox.bbt.V6BBtDetailsActivity;

public class V6BbtAdapter extends BaseAdapter {
    private Context context;
    private List<ProductZT> list;
    DecimalFormat df = new DecimalFormat("###.00");
    DecimalFormat df2 = new DecimalFormat("###0.0");
    private String type;

    public V6BbtAdapter(Context context, List<ProductZT> list, String type) {
        super();
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHelpBbt h;
        if (convertView == null) {
            h = new ViewHelpBbt();
            convertView = LayoutInflater.from(context).inflate(R.layout.v6_bbt_list_item, null);
            h.tv_unit = (TextView) convertView.findViewById(R.id.tv_unit);
            h.tv_name = (TextView) convertView.findViewById(R.id.conduct_name);
            h.tv_time = (TextView) convertView.findViewById(R.id.conduct_time);
            h.tv_rate = (TextView) convertView.findViewById(R.id.conduct_rate);
            h.tv_rateadd = (TextView) convertView.findViewById(R.id.home_rateadd);
            h.tv_investAmount = (TextView) convertView.findViewById(R.id.conduct_investAmount);
            h.tv_newuser = (TextView) convertView.findViewById(R.id.tv_new_user);
            h.li_jiaxi = (LinearLayout) convertView.findViewById(R.id.li_jiaxi);
            h.img_list_status = (ImageView) convertView.findViewById(R.id.img_list_status);
            h.img_pl_reward = (TextView) convertView.findViewById(R.id.img_pl_reward);
//			h.tv_coupon=(TextView)convertView.findViewById(R.id.conduct_addrate);
            h.tv_unit_time = (TextView) convertView.findViewById(R.id.tv_unit_time);
            h.tv_lable = (TextView) convertView.findViewById(R.id.tv_lable);
            convertView.setTag(h);
        } else {
            h = (ViewHelpBbt) convertView.getTag();
        }
//		h.tv_coupon.setVisibility(View.INVISIBLE);
//		h.tv_red.setVisibility(View.VISIBLE);
        ProductZT zt = list.get(position);
        if (list.get(position).getLoanProperty().equals("1")) {
            h.img_pl_reward.setVisibility(View.VISIBLE);
        } else {
            h.img_pl_reward.setVisibility(View.GONE);
        }
        if ("FINISHED".equals(zt.getStatus()) || "SETTLED".equals(zt.getStatus()) || "CLEARED".equals(zt.getStatus())) {
            if (zt.getLoanProperty().equals("3")) {
                h.tv_newuser.setVisibility(View.VISIBLE);
            } else {
                h.tv_newuser.setVisibility(View.GONE);
            }
        } else {
            h.tv_newuser.setVisibility(View.GONE);
        }


//		convertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(context,V6BBtDetailsActivity.class);
//				intent.putExtra("method",list.get(position).getMethod());
//				intent.putExtra("name", list.get(position).getLoanTitle());
//				intent.putExtra("id", list.get(position).getId());
//				intent.putExtra("type", list.get(position).getProductType());
//				intent.putExtra("nametype", type);
//				context.startActivity(intent);
//			}
//		});
        h.tv_name.setText(list.get(position).getLoanTitle());
        if (list.get(position).getRateAdd().equals("0")) {
            list.get(position).setVIS(true);
        }
        if (list.get(position).isVIS()) {
            h.tv_rateadd.setVisibility(View.INVISIBLE);
        } else {
            h.tv_rateadd.setVisibility(View.VISIBLE);
        }
        if (list.get(position).getRateAdd().equals("0")) {
            h.li_jiaxi.setVisibility(View.GONE);
            h.tv_rate.setText(df.format(Double.parseDouble(list.get(position).getOriginRate()) / 100));
        } else {
            h.li_jiaxi.setVisibility(View.VISIBLE);

            h.tv_rate.setText(df.format(Double.parseDouble(list.get(position).getOriginRate()) / 100));
            h.tv_rateadd.setText(df2.format(Double.parseDouble(list.get(position).getRateAdd()) / 100) + "%");
        }
        switch (list.get(position).getStatus()) {

            case "CLEARED":
                h.img_list_status.setVisibility(View.VISIBLE);
                h.img_list_status.setImageBitmap(BitmapUtils.readBitMap(context, R.drawable.yhq2));
                if (list.get(position).getLabelValue() != null && !list.get(position).getLabelValue().equals("")) {
                    h.tv_lable.setVisibility(View.VISIBLE);
                    h.tv_lable.setText(list.get(position).getLabelValue());
                    h.tv_lable.setBackgroundResource(R.drawable.v6_lable_false);
                } else {
                    h.tv_lable.setVisibility(View.GONE);
                }
                h.img_pl_reward.setBackgroundResource(R.drawable.v6_lable_false);
                break;
            case "FINISHED":
                h.img_list_status.setVisibility(View.VISIBLE);
                h.img_list_status.setImageBitmap(BitmapUtils.readBitMap(context, R.drawable.ysq));
                if (list.get(position).getLabelValue() != null && !list.get(position).getLabelValue().equals("")) {
                    h.tv_lable.setVisibility(View.VISIBLE);
                    h.tv_lable.setText(list.get(position).getLabelValue());
                    h.tv_lable.setBackgroundResource(R.drawable.v6_lable_false);
                } else {
                    h.tv_lable.setVisibility(View.GONE);
                }
                h.img_pl_reward.setBackgroundResource(R.drawable.v6_lable_false);
                break;
            case "SETTLED":
                h.img_list_status.setVisibility(View.VISIBLE);
                h.img_list_status.setImageBitmap(BitmapUtils.readBitMap(context, R.drawable.hkz));
                if (list.get(position).getLabelValue() != null && !list.get(position).getLabelValue().equals("")) {
                    h.tv_lable.setVisibility(View.VISIBLE);
                    h.tv_lable.setText(list.get(position).getLabelValue());
                    h.tv_lable.setBackgroundResource(R.drawable.v6_lable_false);
                } else {
                    h.tv_lable.setVisibility(View.GONE);
                }
                h.img_pl_reward.setBackgroundResource(R.drawable.v6_lable_false);
                break;
            case "SCHEDULED":
                if (list.get(position).getLabelValue() != null && !list.get(position).getLabelValue().equals("")) {
                    h.tv_lable.setVisibility(View.VISIBLE);
                    h.tv_lable.setText(list.get(position).getLabelValue());
                    h.tv_lable.setBackgroundResource(R.drawable.v6_lable_false);
                } else {
                    h.tv_lable.setVisibility(View.GONE);
                }
                h.img_list_status.setVisibility(View.INVISIBLE);
                h.img_pl_reward.setBackgroundResource(R.drawable.v6_lable_false);
                break;
            default:
                if (list.get(position).getLabelValue() != null && !list.get(position).getLabelValue().equals("")) {
                    h.tv_lable.setVisibility(View.VISIBLE);
                    h.tv_lable.setBackgroundResource(R.drawable.v6_new_user);
                    h.tv_lable.setText(list.get(position).getLabelValue());
                } else {
                    h.tv_lable.setVisibility(View.GONE);
                }
                h.img_pl_reward.setBackgroundResource(R.drawable.v6_new_user);
                h.img_list_status.setVisibility(View.INVISIBLE);
        }
        if (!list.get(position).getYears().equals("0")) {
            h.tv_time.setText(Integer.parseInt(list.get(position).getYears()) * 12+"");
            h.tv_unit_time.setText("个月");
        } else {
            if (!list.get(position).getMonths().equals("0")) {
                h.tv_time.setText(Integer.parseInt(list.get(position).getMonths()) + "");
                h.tv_unit_time.setText("个月");
            } else {
                h.tv_time.setText(Integer.parseInt(list.get(position).getDays()) + "");
                h.tv_unit_time.setText("天");
            }
        }
//		if(list.get(position).getMonths().equals("0")){
//			if(!list.get(position).getYears().equals("0")){
//				if(!list.get(position).getDays().equals("0")){
//					h.tv_time.setText(Integer.parseInt(list.get(position).getYears())*12+"个月"+list.get(position).getDays()+"天");
//				}else{
//
//					h.tv_time.setText(Integer.parseInt(list.get(position).getYears())*12+"个月");
//				}
//			}else{
//
//				h.tv_time.setText(list.get(position).getDays()+"天");
//			}
//		}else{
//			if(list.get(position).getDays().equals("0")){
//				if(!list.get(position).getYears().equals("0")){
//					h.tv_time.setText((Integer.parseInt(list.get(position).getYears())*12)+Integer.parseInt(list.get(position).getMonths())+"个月");
//
//				}else{
//					h.tv_time.setText(+Integer.parseInt(list.get(position).getMonths())+"个月");
//				}
//			}else{
//				if(!list.get(position).getYears().equals("0")){
//					h.tv_time.setText((Integer.parseInt(list.get(position).getYears())*12)+Integer.parseInt(list.get(position).getMonths())+"个月"+list.get(position).getDays()+"天");
//				}else{
//
//					h.tv_time.setText(list.get(position).getMonths()+"个月"+list.get(position).getDays()+"天");
//				}
//			}
//		}
        if (Double.parseDouble(list.get(position).getAmount()) - Double.parseDouble(list.get(position).getInvestAmount()) < 100) {
            h.tv_investAmount.setText(Double.parseDouble(list.get(position).getAmount()) - Double.parseDouble(list.get(position).getInvestAmount()) + "元");
            h.tv_unit.setVisibility(View.GONE);
        } else {
            h.tv_investAmount.setText(Regular.getDecimalFormatTwo((Double.parseDouble(list.get(position).getAmount()) - Double.parseDouble(list.get(position).getInvestAmount())) / 10000) + "");
            h.tv_unit.setVisibility(View.VISIBLE);
        }


//		if(list.get(position).getStatus().equals("OPENED")){
//			
//			h.btn_invest.setText("马上投资");
//			h.btn_invest.setBackgroundResource(R.drawable.my_button);
//		}else if(list.get(position).getStatus().equals("SETTLED")){
//			h.btn_invest.setText("还款中");
//			h.btn_invest.setBackgroundResource(R.drawable.btn_unclick);
//		}else{
//			h.btn_invest.setText("已售罄");
//			h.btn_invest.setBackgroundResource(R.drawable.btn_unclick);
//		}
        return convertView;
    }

    class ViewHelpBbt {
        private TextView tv_name, tv_coupon, tv_rate, tv_rateadd, tv_time, tv_investAmount, tv_newuser, tv_coupon_type;
        private LinearLayout li_jiaxi, li_newuser;
        private ImageView img_list_status;
        private TextView tv_red, img_pl_reward, tv_unit, tv_unit_time, tv_lable;
        private Button btn_invest;
    }
}
