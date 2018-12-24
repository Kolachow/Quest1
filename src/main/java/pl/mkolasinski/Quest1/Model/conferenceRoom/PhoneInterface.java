package pl.mkolasinski.Quest1.Model.conferenceRoom;

public enum PhoneInterface {
    USB(0, "USB"), BLUETOOTH(1, "bluetooth"),
    NO_INTERFACE(2, "No interface");

    private int phoneInterfaceId;
    private String phoneInterfaceName;

    PhoneInterface(int phoneInterfaceId, String phoneInterfaceName) {
        this.phoneInterfaceId = phoneInterfaceId;
        this.phoneInterfaceName = phoneInterfaceName;
    }

    public int getPhoneInterfaceId() {
        return phoneInterfaceId;
    }

    public PhoneInterface setPhoneInterfaceId(int phoneInterfaceId) {
        this.phoneInterfaceId = phoneInterfaceId;
        return this;
    }

    public String getPhoneInterfaceName() {
        return phoneInterfaceName;
    }

    public PhoneInterface setPhoneInterfaceName(String phoneInterfaceName) {
        this.phoneInterfaceName = phoneInterfaceName;
        return this;
    }
}
