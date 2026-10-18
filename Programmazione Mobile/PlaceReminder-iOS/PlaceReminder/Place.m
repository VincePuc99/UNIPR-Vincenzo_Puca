//
//  Place.m
//  PlaceReminder
//
//  Created by Vincenzo Puca on 07/06/23.
//

#import "Place.h"

@implementation Place

- (instancetype)initWithName:(NSString *)name latitude:(CLLocationDegrees)latitude longitude:(CLLocationDegrees)longitude description:(NSString *)descriptionText dateAdded:(NSDate *)dateAdded customName:(NSString *)customName {
    self = [super init];
    if (self) {
        _name = name;
        _latitude = latitude;
        _longitude = longitude;
        _descriptionText = descriptionText;
        _dateAdded = dateAdded;
        _customName = customName;
    }
    return self;
}


#pragma mark - NSCoding

+ (BOOL)supportsSecureCoding {
    return YES;
}

- (instancetype)initWithCoder:(NSCoder *)coder {
    self = [super init];
    if (self) {
        _name = [coder decodeObjectForKey:@"name"];
        _latitude = [coder decodeDoubleForKey:@"latitude"];
        _longitude = [coder decodeDoubleForKey:@"longitude"];
        _descriptionText = [coder decodeObjectForKey:@"descriptionText"];
        _dateAdded = [coder decodeObjectForKey:@"dateAdded"];
        _customName = [coder decodeObjectForKey:@"customName"];;
    }
    return self;
}

- (void)encodeWithCoder:(NSCoder *)coder {
    [coder encodeObject:self.name forKey:@"name"];
    [coder encodeDouble:self.latitude forKey:@"latitude"];
    [coder encodeDouble:self.longitude forKey:@"longitude"];
    [coder encodeObject:self.descriptionText forKey:@"descriptionText"];
    [coder encodeObject:self.dateAdded forKey:@"dateAdded"];
    [coder encodeObject:self.customName forKey:@"customName"];
}

@end
