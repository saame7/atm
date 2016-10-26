package ca.ulaval.glo4002.atm.infrastructure.dispensers;

import java.nio.ByteBuffer;
import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;
import javax.usb.UsbPipe;

import ca.ulaval.glo4002.atm.domain.dispensers.CashDispenser;

public class SpxModel3CashDispenser implements CashDispenser {

    @Override
    public boolean canAccomodateWithdrawal(double amount) {
        return amount % 20 == 0;
    }

    public void dispense(double amount) {
        UsbDevice device = findCashDispenserUsbDevice();
        UsbInterface usbInterface = device.getActiveUsbConfiguration().getUsbInterface((byte) 1);
        UsbPipe usbPipe = usbInterface.getUsbEndpoint((byte) 0x02).getUsbPipe();
        try {
            byte[] bytes = new byte[Double.BYTES];
            ByteBuffer.wrap(bytes).putDouble(amount);
            usbPipe.syncSubmit(bytes);
        } catch (UsbNotActiveException e) {
            throw new UnableToFindCashDispenser(e);
        } catch (UsbNotOpenException e) {
            throw new UnableToFindCashDispenser(e);
        } catch (Exception e) {
            throw new UnableToFindCashDispenser(e);
        } finally {
            try {
                usbPipe.close();
            } catch (Exception e) {
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private UsbDevice findCashDispenserUsbDevice() {
        for (UsbDevice device : (List<UsbDevice>) getRootUsbHub().getAttachedUsbDevices()) {
            if (device.getActiveUsbConfigurationNumber() == 0x1f1b) {
                return device;
            }
        }

        throw new UnableToFindCashDispenser();
    }

    private UsbHub getRootUsbHub() {
        try {
            return UsbHostManager.getUsbServices().getRootUsbHub();
        } catch (SecurityException e) {
            throw new UnableToFindCashDispenser(e);
        } catch (UsbException e) {
            throw new UnableToFindCashDispenser(e);
        }
    }

}
