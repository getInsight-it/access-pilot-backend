package it.getinsight.utilitario;

public final class PropertyPathConstants {
    private PropertyPathConstants() {}

    public static final class Common {
        public static final String ID = "id";
        public static final String PARENT_ID = "parentId";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String EXTERNAL_CODE = "externalCode";

        private Common() {}
    }

    public static final class AttachmentConfigurationCSV {
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String ALLOWED_EXTENSIONS = "allowedExtensions";
        public static final String ICON = "icon";
        public static final String REQUIRED = "required";
        public static final String KEY = "key";


        private AttachmentConfigurationCSV() {}
    }

    public static final class Item {
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String EXTERNAL_CODE = "externalCode";
        public static final String[] CSV_HEADERS = {
            Common.ID,
            Common.PARENT_ID,
            Common.NAME,
            Common.DESCRIPTION,
            Common.EXTERNAL_CODE
        };

        private Item() {}
    }

    public static final class Role {
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String CLIENT_ID = "client.id";

        private Role() {}
    }
}
