#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface TimePickerSwiftViewWrapper : NSObject
- (instancetype)init;
- (void)setMinuteInterval:(NSInteger)minuteInterval;
- (void)setLocale:(NSString *)locale;
- (void)setValue:(double)timestamp;
- (void)setTextColor:(UIColor *)color;
- (void)setOnChange:(void(^)(double timestamp))callback;
@property (nonatomic, readonly) UIView *view;
@end

NS_ASSUME_NONNULL_END
