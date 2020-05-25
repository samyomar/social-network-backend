package com.samy.socialnetwork.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.samy.socialnetwork.web.rest.TestUtil;

public class ProfilePhotosTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfilePhotos.class);
        ProfilePhotos profilePhotos1 = new ProfilePhotos();
        profilePhotos1.setId(1L);
        ProfilePhotos profilePhotos2 = new ProfilePhotos();
        profilePhotos2.setId(profilePhotos1.getId());
        assertThat(profilePhotos1).isEqualTo(profilePhotos2);
        profilePhotos2.setId(2L);
        assertThat(profilePhotos1).isNotEqualTo(profilePhotos2);
        profilePhotos1.setId(null);
        assertThat(profilePhotos1).isNotEqualTo(profilePhotos2);
    }
}
