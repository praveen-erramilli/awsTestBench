package lambda;

public class ProvisionRequest {
    String allocatedStorage;
    String dBInstanceClass;
    String dBName;
    String engine;
    String masterUsername;
    String masterUserPassword;

    public ProvisionRequest(String allocatedStorage, String dBInstanceClass, String dBName, String engine, String masterUsername, String masterUserPassword) {
        this.allocatedStorage = allocatedStorage;
        this.dBInstanceClass = dBInstanceClass;
        this.dBName = dBName;
        this.engine = engine;
        this.masterUsername = masterUsername;
        this.masterUserPassword = masterUserPassword;
    }

    public ProvisionRequest() {
    }

    public String getAllocatedStorage() {
        return allocatedStorage;
    }

    public void setAllocatedStorage(String allocatedStorage) {
        this.allocatedStorage = allocatedStorage;
    }

    public String getdBInstanceClass() {
        return dBInstanceClass;
    }

    public void setdBInstanceClass(String dBInstanceClass) {
        this.dBInstanceClass = dBInstanceClass;
    }

    public String getdBName() {
        return dBName;
    }

    public void setdBName(String dBName) {
        this.dBName = dBName;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getMasterUsername() {
        return masterUsername;
    }

    public void setMasterUsername(String masterUsername) {
        this.masterUsername = masterUsername;
    }

    public String getMasterUserPassword() {
        return masterUserPassword;
    }

    public void setMasterUserPassword(String masterUserPassword) {
        this.masterUserPassword = masterUserPassword;
    }

    @Override
    public String toString() {
        return "ProvisionRequest{" +
                "allocatedStorage='" + allocatedStorage + '\'' +
                ", dBInstanceClass='" + dBInstanceClass + '\'' +
                ", dBName='" + dBName + '\'' +
                ", engine='" + engine + '\'' +
                ", masterUsername='" + masterUsername + '\'' +
                ", masterUserPassword='" + masterUserPassword + '\'' +
                '}';
    }
}