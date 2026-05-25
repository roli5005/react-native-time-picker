#import "TimePickerSwiftViewWrapper.h"
#import "TimePicker-Swift.h"

@implementation TimePickerSwiftViewWrapper {
    TimePickerViewBridge *_bridge;
}

- (instancetype)init {
    if (self = [super init]) {
        _bridge = [[TimePickerViewBridge alloc] init];
    }
    return self;
}

- (UIView *)view                              { return _bridge.view; }
- (void)setMinuteInterval:(NSInteger)v        { [_bridge setMinuteInterval:v]; }
- (void)setLocale:(NSString *)v               { [_bridge setLocale:v]; }
- (void)setValue:(double)v                    { [_bridge setValue:v]; }
- (void)setTextColor:(UIColor *)v              { [_bridge setTextColor:v]; }
- (void)setOnChange:(void(^)(double))callback { [_bridge setOnChange:callback]; }

@end
