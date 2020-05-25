import { IUser } from 'app/shared/model/user.model';
import { IFollow } from 'app/shared/model/follow.model';
import { IPost } from 'app/shared/model/post.model';
import { IProfilePhotos } from 'app/shared/model/profile-photos.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { Language } from 'app/shared/model/enumerations/language.model';

export interface IUserProfile {
  id?: number;
  firstName?: string;
  lastName?: string;
  age?: number;
  gender?: Gender;
  country?: string;
  nativeLang?: Language;
  user?: IUser;
  followings?: IFollow[];
  followers?: IFollow[];
  posts?: IPost[];
  photos?: IProfilePhotos[];
  following2s?: IUserProfile[];
  follower2s?: IUserProfile[];
}

export const defaultValue: Readonly<IUserProfile> = {};
