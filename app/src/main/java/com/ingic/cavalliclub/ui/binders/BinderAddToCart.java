package com.ingic.cavalliclub.ui.binders;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.ingic.cavalliclub.R;
import com.ingic.cavalliclub.activities.DockActivity;
import com.ingic.cavalliclub.activities.MainActivity;
import com.ingic.cavalliclub.entities.AddCartEntity;
import com.ingic.cavalliclub.fragments.AddToCartFragment;
import com.ingic.cavalliclub.helpers.UIHelper;
import com.ingic.cavalliclub.interfaces.TotalSum;
import com.ingic.cavalliclub.ui.adapters.ArrayListAdapter;
import com.ingic.cavalliclub.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.cavalliclub.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class BinderAddToCart extends ViewBinder<AddCartEntity> {

    @BindView(R.id.tv_drink_name)
    AnyTextView tvDrinkName;
    @BindView(R.id.iv_plus)
    ImageView ivPlus;
    @BindView(R.id.tv_qty)
    AnyTextView tvQty;
    @BindView(R.id.iv_minus)
    ImageView ivMinus;
    @BindView(R.id.tv_amount)
    AnyTextView tvAmount;
    private DockActivity context;
    private ImageLoader imageLoader;
    int Count;
    int MultiplyAmount;
    TotalSum totalSum;
    AddCartEntity addCartEntity;
    MainActivity mainActivity;
    public Realm realm;
    private ArrayListAdapter<AddCartEntity> adapter;
    AddToCartFragment addToCartFragment;

    public BinderAddToCart(DockActivity context, TotalSum totalSum, MainActivity mainActivity, ArrayListAdapter<AddCartEntity> adapter) {
        super(R.layout.item_lv_add_to_cart);
        imageLoader = ImageLoader.getInstance();
        this.context = context;
        this.totalSum = totalSum;
        realm = Realm.getDefaultInstance();
        this.adapter = adapter;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final AddCartEntity entity, final int position, int grpPosition, View view, Activity activity) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        realm = Realm.getDefaultInstance();
        MultiplyAmount = 0;
        Count = 0;
        if (entity != null) {
            if (entity.getProductName() != null)
                viewHolder.tvDrinkName.setText(entity.getProductName());
            if (entity.getProductQuantity() != 0)
                viewHolder.tvQty.setText(entity.getProductQuantity() + "");
        }
        if (!viewHolder.tvQty.getText().toString().equalsIgnoreCase("")) {
            Count = Integer.parseInt(viewHolder.tvQty.getText().toString());
        }
        if (entity != null && entity.getProductPrice() != null) {
            MultiplyAmount = Integer.valueOf(entity.getProductPrice()) * Count;
        }
        viewHolder.tvAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(MultiplyAmount));
        totalSum.sum(MultiplyAmount, position);


        viewHolder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Count = Integer.parseInt(viewHolder.tvQty.getText().toString());
                Count = Count + 1;
                viewHolder.tvQty.setText(Count + "");
                MultiplyAmount = Integer.valueOf(entity.getProductPrice()) * Count;
                viewHolder.tvAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(MultiplyAmount));
                totalSum.addAmount(Integer.parseInt(entity.getProductPrice()), position, MultiplyAmount);

                addCartEntity = new AddCartEntity();
                AddCartEntity entityRealm = null;
                try {
                    entityRealm = realm
                            .where(AddCartEntity.class)
                            .equalTo("ProductName", entity.getProductName()).findFirst();
                    //.equalTo("Id", entity.getId()).findFirst();
                    addCartEntity.setProductName(entity.getProductName());
                    addCartEntity.setProductPrice(entity.getProductPrice());
                    addCartEntity.setId(entity.getId());
                    addCartEntity.setProductQuantity(entityRealm == null ? 1 : (entityRealm.getProductQuantity() + 1));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                final AddCartEntity finalEntityRealm = entityRealm;
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        try {
                            realm.copyToRealmOrUpdate(addCartEntity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        viewHolder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Count = Integer.parseInt(viewHolder.tvQty.getText().toString());
                if (!(Count <= 1)) {
                    Count = Count - 1;
                    viewHolder.tvQty.setText(Count + "");
                    MultiplyAmount = Integer.valueOf(entity.getProductPrice()) * Count;
                    viewHolder.tvAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(MultiplyAmount));
                    totalSum.substractAmount(Integer.parseInt(entity.getProductPrice()), position, MultiplyAmount);

                    addCartEntity = new AddCartEntity();
                    AddCartEntity entityRealm = null;
                    try {
                        entityRealm = realm
                                .where(AddCartEntity.class)
                                //.equalTo("Id", entity.getId()).findFirst();
                                .equalTo("ProductName", entity.getProductName()).findFirst();
                        addCartEntity.setProductName(entity.getProductName());
                        addCartEntity.setProductPrice(entity.getProductPrice());
                        addCartEntity.setId(entity.getId());
                        addCartEntity.setProductQuantity(entityRealm == null ? 1 : (entityRealm.getProductQuantity() - 1));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final AddCartEntity finalEntityRealm = entityRealm;
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            try {
                                realm.copyToRealmOrUpdate(addCartEntity);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {


                    Count = Count - 1;
                    viewHolder.tvQty.setText(Count + "");
                    MultiplyAmount = Integer.valueOf(entity.getProductPrice()) * Count;
                    viewHolder.tvAmount.setText("AED " + NumberFormat.getNumberInstance(Locale.US).format(MultiplyAmount));
                    totalSum.substractAmount(Integer.parseInt(entity.getProductPrice()), position, MultiplyAmount);

                    addCartEntity = new AddCartEntity();
                    AddCartEntity entityRealm = null;

                    try {
                        entityRealm = realm
                                .where(AddCartEntity.class)
                                //.equalTo("Id", entity.getId()).findFirst();
                                .equalTo("ProductName", entity.getProductName()).findFirst();
                        addCartEntity.setProductName(entity.getProductName());
                        addCartEntity.setProductPrice(entity.getProductPrice());
                        addCartEntity.setId(entity.getId());
                        addCartEntity.setProductQuantity(entityRealm == null ? 1 : (entityRealm.getProductQuantity() - 1));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final String id = entity.getId();
                    final String productName = entity.getProductName();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            try {
                                realm.where(AddCartEntity.class).equalTo("Id", id).findAll().deleteAllFromRealm();
                                totalSum.onClickAdapterNotify(position);
                                UIHelper.showShortToastInCenter(context, "Selected item has been removed.");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_drink_name)
        AnyTextView tvDrinkName;
        @BindView(R.id.iv_plus)
        ImageView ivPlus;
        @BindView(R.id.tv_qty)
        AnyTextView tvQty;
        @BindView(R.id.iv_minus)
        ImageView ivMinus;
        @BindView(R.id.tv_amount)
        AnyTextView tvAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}