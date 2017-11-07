package zpcaifu.kotlintest

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


import java.text.DecimalFormat

import zpcaifu.kotlintest.utils.BitmapUtils
import zpcaifu.kotlintest.utils.Regular

//import com.rmbbox.bbt.V6BBtDetailsActivity;

class KotlinAdapter(private val context: Context, private val list: List<ProductZT>, private val type: String) : BaseAdapter() {
    internal var df = DecimalFormat("###.00")
    internal var df2 = DecimalFormat("###0.0")

    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return list.size
    }

    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // TODO Auto-generated method stub
        val h: ViewHelpBbt
        if (convertView == null) {
            h = ViewHelpBbt()
            convertView = LayoutInflater.from(context).inflate(R.layout.v6_bbt_list_item, null)
            h.tv_unit = convertView!!.findViewById(R.id.tv_unit) as TextView
            h.tv_name = convertView.findViewById(R.id.conduct_name) as TextView
            h.tv_time = convertView.findViewById(R.id.conduct_time) as TextView
            h.tv_rate = convertView.findViewById(R.id.conduct_rate) as TextView
            h.tv_rateadd = convertView.findViewById(R.id.home_rateadd) as TextView
            h.tv_investAmount = convertView.findViewById(R.id.conduct_investAmount) as TextView
            h.tv_newuser = convertView.findViewById(R.id.tv_new_user) as TextView
            h.li_jiaxi = convertView.findViewById(R.id.li_jiaxi) as LinearLayout
            h.img_list_status = convertView.findViewById(R.id.img_list_status) as ImageView
            h.img_pl_reward = convertView.findViewById(R.id.img_pl_reward) as TextView
            //			h.tv_coupon=(TextView)convertView.findViewById(R.id.conduct_addrate);
            h.tv_unit_time = convertView.findViewById(R.id.tv_unit_time) as TextView
            h.tv_lable = convertView.findViewById(R.id.tv_lable) as TextView

            convertView.tag = h
        } else {
            h = convertView.tag as ViewHelpBbt
        }
        //		h.tv_coupon.setVisibility(View.INVISIBLE);
        //		h.tv_red.setVisibility(View.VISIBLE);
        val zt = list[position]
        if (list[position].loanProperty == "1") {
            h.img_pl_reward!!.visibility = View.VISIBLE
        } else {
            h.img_pl_reward!!.visibility = View.GONE
        }
        if ("FINISHED" == zt.status || "SETTLED" == zt.status || "CLEARED" == zt.status) {
            if (zt.loanProperty == "3") {
                h.tv_newuser!!.visibility = View.VISIBLE
            } else {
                h.tv_newuser!!.visibility = View.GONE
            }
        } else {
            h.tv_newuser!!.visibility = View.GONE
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
        h.tv_name!!.text = list[position].loanTitle
        if (list[position].rateAdd == "0") {
            list[position].isVIS = true
        }
        if (list[position].isVIS) {
            h.tv_rateadd!!.visibility = View.INVISIBLE
        } else {
            h.tv_rateadd!!.visibility = View.VISIBLE
        }
        if (list[position].rateAdd == "0") {
            h.li_jiaxi!!.visibility = View.GONE
            h.tv_rate!!.text = df.format(java.lang.Double.parseDouble(list[position].originRate) / 100)
        } else {
            h.li_jiaxi!!.visibility = View.VISIBLE

            h.tv_rate!!.text = df.format(java.lang.Double.parseDouble(list[position].originRate) / 100)
            h.tv_rateadd!!.text = df2.format(java.lang.Double.parseDouble(list[position].rateAdd) / 100) + "%"
        }
        when (list[position].status) {

            "CLEARED" -> {
                h.img_list_status!!.visibility = View.VISIBLE
                h.img_list_status!!.setImageBitmap(BitmapUtils.readBitMap(context, R.drawable.yhq2))
                if (list[position].labelValue != null && list[position].labelValue != "") {
                    h.tv_lable!!.visibility = View.VISIBLE
                    h.tv_lable!!.text = list[position].labelValue
                    h.tv_lable!!.setBackgroundResource(R.drawable.v6_lable_false)
                } else {
                    h.tv_lable!!.visibility = View.GONE
                }
                h.img_pl_reward!!.setBackgroundResource(R.drawable.v6_lable_false)
            }
            "FINISHED" -> {
                h.img_list_status!!.visibility = View.VISIBLE
                h.img_list_status!!.setImageBitmap(BitmapUtils.readBitMap(context, R.drawable.ysq))
                if (list[position].labelValue != null && list[position].labelValue != "") {
                    h.tv_lable!!.visibility = View.VISIBLE
                    h.tv_lable!!.text = list[position].labelValue
                    h.tv_lable!!.setBackgroundResource(R.drawable.v6_lable_false)
                } else {
                    h.tv_lable!!.visibility = View.GONE
                }
                h.img_pl_reward!!.setBackgroundResource(R.drawable.v6_lable_false)
            }
            "SETTLED" -> {
                h.img_list_status!!.visibility = View.VISIBLE
                h.img_list_status!!.setImageBitmap(BitmapUtils.readBitMap(context, R.drawable.hkz))
                if (list[position].labelValue != null && list[position].labelValue != "") {
                    h.tv_lable!!.visibility = View.VISIBLE
                    h.tv_lable!!.text = list[position].labelValue
                    h.tv_lable!!.setBackgroundResource(R.drawable.v6_lable_false)
                } else {
                    h.tv_lable!!.visibility = View.GONE
                }
                h.img_pl_reward!!.setBackgroundResource(R.drawable.v6_lable_false)
            }
            "SCHEDULED" -> {
                if (list[position].labelValue != null && list[position].labelValue != "") {
                    h.tv_lable!!.visibility = View.VISIBLE
                    h.tv_lable!!.text = list[position].labelValue
                    h.tv_lable!!.setBackgroundResource(R.drawable.v6_lable_false)
                } else {
                    h.tv_lable!!.visibility = View.GONE
                }
                h.img_list_status!!.visibility = View.INVISIBLE
                h.img_pl_reward!!.setBackgroundResource(R.drawable.v6_lable_false)
            }
            else -> {
                if (list[position].labelValue != null && list[position].labelValue != "") {
                    h.tv_lable!!.visibility = View.VISIBLE
                    h.tv_lable!!.setBackgroundResource(R.drawable.v6_new_user)
                    h.tv_lable!!.text = list[position].labelValue
                } else {
                    h.tv_lable!!.visibility = View.GONE
                }
                h.img_pl_reward!!.setBackgroundResource(R.drawable.v6_new_user)
                h.img_list_status!!.visibility = View.INVISIBLE
            }
        }
        if (list[position].years != "1") {
            var year=1
            var c=12*1
            h.tv_time!!.text="$c"
            h.tv_unit_time!!.text = "个月"
        } else {
            if (list[position].months != "0") {
                var month=Integer.parseInt(list[position].months)
                h.tv_time!!.text="$month"
                h.tv_unit_time!!.text = "个月"
            } else {
                var days=Integer.parseInt(list[position].days)
                h.tv_time!!.text="$days"
                h.tv_unit_time!!.text = "天"
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
        if (java.lang.Double.parseDouble(list[position].amount) - java.lang.Double.parseDouble(list[position].investAmount) < 100) {
            var a :Double=java.lang.Double.parseDouble(list[position].amount)
            var b :Double=java.lang.Double.parseDouble(list[position].investAmount)
            var c= Regular.getDecimalFormatTwo((a - b) / 10000)
            h.tv_investAmount!!.text="$c 元"
            h.tv_unit!!.visibility = View.GONE

        } else {
            h.tv_investAmount!!.text = Regular.getDecimalFormatTwo((java.lang.Double.parseDouble(list[position].amount) - java.lang.Double.parseDouble(list[position].investAmount)) / 10000)
            h.tv_unit!!.visibility = View.VISIBLE
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
        return convertView
    }

    internal inner class ViewHelpBbt {
        internal var tv_name: TextView? = null
        internal var tv_coupon: TextView? = null
        internal var tv_rate: TextView? = null
        internal var tv_rateadd: TextView? = null
        internal var tv_time: TextView? = null
        internal var tv_investAmount: TextView? = null
        internal var tv_newuser: TextView? = null
        internal var tv_coupon_type: TextView? = null
        internal var li_jiaxi: LinearLayout? = null
        internal var li_newuser: LinearLayout? = null
        internal var img_list_status: ImageView? = null
        internal var tv_red: TextView? = null
        internal var img_pl_reward: TextView? = null
        internal var tv_unit: TextView? = null
        internal var tv_unit_time: TextView? = null
        internal var tv_lable: TextView? = null
        internal var btn_invest: Button? = null
    }
}
