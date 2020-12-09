package net.onest.timestoryprj.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.Pricing;

import java.util.List;

public class RechargeAdapter extends BaseAdapter {
    private List<Pricing> pricings;
    private Context mContext;

    @Override
    public int getCount() {
        return pricings.size();
    }

    @Override
    public Object getItem(int i) {
        return pricings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_recharge_rank, null);
            holder = new ViewHolder();
            holder.rechargeIntegral = view.findViewById(R.id.recharge_integral);
            holder.rechargeMoney = view.findViewById(R.id.recharge_money);
            holder.btnRecharge = view.findViewById(R.id.btn_recharge);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.rechargeIntegral.setText(pricings.get(position).getPricingCount() + "积分");
        holder.rechargeMoney.setText(pricings.get(position).getPricingMoney() + "元");
        holder.btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 调用支付宝接口充值
                Toast.makeText(mContext, "您要充值" + pricings.get(position).getPricingMoney() + "元", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public RechargeAdapter(Context context, List<Pricing> pricings) {
        this.pricings = pricings;
        this.mContext = context;
    }

    public class ViewHolder {
        private TextView rechargeMoney;
        private TextView rechargeIntegral;
        private Button btnRecharge;
    }
}
