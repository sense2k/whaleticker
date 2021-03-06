package gui.market;

import utils.Formatter;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.Normalizer;

public class MarketOrderCell extends AbstractCellEditor implements TableCellRenderer {

    private JPanel panel;
    private JLabel size;
    private JLabel price;
    private JLabel instrument;
    private JLabel slip;
    private JLabel btcAmt;

    MarketOrderCell() {

        size = new JLabel();

        instrument = new JLabel();
        instrument.setForeground(Color.GRAY);
        instrument.setFont(new Font(null, Font.ITALIC, 12));

        slip = new JLabel();
        slip.setForeground(Color.BLACK);
        slip.setFont(new Font(null, Font.PLAIN, 11));

        price = new JLabel();
        price.setForeground(Color.DARK_GRAY);
        price.setFont(new Font(null, Font.PLAIN, 10));

        btcAmt = new JLabel();
        btcAmt.setForeground(Color.DARK_GRAY);
        btcAmt.setFont(new Font(null, Font.ITALIC, 11));

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(size);
        panel.add(slip);
        panel.add(price);
        panel.add(btcAmt);
        panel.add(instrument);
    }


    private void updateData(MarketOrder order) {

        size.setText(Formatter.kFormat((double) Math.abs(order.getAmt()), 0) + " ");
        size.setIcon(getIcon(order.getExchange()));
        setInstrument(order);
        setSlip(order);
        setBtcAmt(order);
        setPrice(order);

        panel.setBackground(getColor(order.getAmt()));

        int orderAmt = Math.abs(order.getAmt());

        int up = 1;

        //do this better
        if (orderAmt < 1000) {
            panel.setBorder(null);
            size.setForeground(Color.GRAY);
            size.setFont(new Font(null, Font.ITALIC, 12 + up));

        } else if (orderAmt < 10000) {
            panel.setBorder(null);
            size.setForeground(Color.DARK_GRAY);
            size.setFont(new Font(null, Font.ITALIC, 13 + up));

        } else if (orderAmt < 100000) {
            panel.setBorder(null);
            size.setForeground(Color.BLACK);
            size.setFont(new Font(null, Font.PLAIN, 15 + up));

        } else if (orderAmt < 500000) {
            panel.setBorder(null);
            size.setForeground(Color.BLACK);
            size.setFont(new Font(null, Font.PLAIN, 17 + up));

        } else if (orderAmt < 1000000) {
            panel.setBorder(null);
            size.setForeground(Color.WHITE);
            size.setFont(new Font(null, Font.BOLD, 17 + up));

        } else { //over 1mil
            panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            size.setForeground(Color.YELLOW);
            size.setFont(new Font(null, Font.BOLD, 17 + up));
        }


    }

    private void setPrice(MarketOrder order) {

        if (order.isShowPrices() && order.getLastPrice() > 0) {

            double pricedub = Formatter.round(order.getLastPrice(), 1);
            String pricestr = String.valueOf(pricedub);

            price.setText("" + (pricedub % 1 == 0 ? pricestr.substring(0, pricestr.length() - 2) : pricedub));

        } else {
            price.setText("");
        }

    }

    private void setBtcAmt(MarketOrder order) {

        btcAmt.setText("");

        if (order.getExchange().equals("bitfinex") || order.getExchange().equals("binance") || order.getExchange().equals("gdax")) {
            if (order.getLastPrice() != 0 && Math.abs(order.getAmt()) > 90000) {
                btcAmt.setText(" [" + Formatter.lowFormat(order.getAmt() / order.getLastPrice()) + " btc]");
            }
        }

    }

    private void setSlip(MarketOrder order) {

        int slipInt = order.getSlip();

        if (Math.abs(slipInt) >= 1) {

            slip.setText(String.valueOf(Math.abs(slipInt))+ " ");

            if (slipInt > 0) {
                slip.setIcon(new ImageIcon(getClass().getResource("/uparrow.png")));
            } else {
                slip.setIcon(new ImageIcon(getClass().getResource("/downarrow.png")));
            }

        } else {
            slip.setText("");
            slip.setIcon(null);
        }
    }

    private void setInstrument(MarketOrder order) {

        String in = order.getInstrument();

        switch (in) {
            case "bitmexDec":
                instrument.setText(" december");
                break;
            case "bitmexSept":
                instrument.setText(" september");
                break;
            case "okexThis":
                instrument.setText(" this week");
                break;
            case "okexNext":
                instrument.setText(" next week");
                break;
            case "okexQuat":
                instrument.setText(" quarterly");
                break;
            default:
                instrument.setText("");
                break;
        }
    }

    private Icon getIcon(String exchange) {

        ImageIcon icon = null;

        switch (exchange) {
            case "bitmex":
                icon = new ImageIcon(getClass().getResource("/bitmex22.png"));
                break;
            case "bitfinex":
                icon = new ImageIcon(getClass().getResource("/bitfinex22.png"));
                break;
            case "okex":
                icon = new ImageIcon(getClass().getResource("/okex22.png"));
                break;
            case "binance":
                icon = new ImageIcon(getClass().getResource("/binance.png"));
                break;
            case "gdax":
                icon = new ImageIcon(getClass().getResource("/gdax25.png"));
                break;
        }

        return icon;
    }

    private static Color getColor(int amt) {

        int intensity = Math.abs(amt) / 4200;

        if (intensity > 165) {
            intensity = 165;
        } else if (intensity < 1) {
            intensity = 1;
        }

        if (amt > 0) {
            return new Color(170 - intensity, 255 - intensity / 2, 170 - intensity);
        } else {
            return new Color(255 - intensity / 2, 170 - intensity, 170 - intensity);
        }

    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        MarketOrder order = (MarketOrder) value;
        updateData(order);
        return panel;
    }


}
